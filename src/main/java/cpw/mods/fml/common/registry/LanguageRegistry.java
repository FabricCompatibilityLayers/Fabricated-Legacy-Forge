/*
 * The FML Forge Mod Loader suite.
 * Copyright (C) 2012 cpw
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package cpw.mods.fml.common.registry;

import com.google.common.base.Charsets;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Language;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

public class LanguageRegistry {
    private static final LanguageRegistry INSTANCE = new LanguageRegistry();
    private Map<String, Properties> modLanguageData = new HashMap();

    public LanguageRegistry() {
    }

    public static LanguageRegistry instance() {
        return INSTANCE;
    }

    public String getStringLocalization(String key) {
        return this.getStringLocalization(key, Language.getInstance().getCode());
    }

    public String getStringLocalization(String key, String lang) {
        String localizedString = "";
        Properties langPack = (Properties)this.modLanguageData.get(lang);
        if (langPack != null && langPack.getProperty(key) != null) {
            localizedString = langPack.getProperty(key);
        }

        return localizedString;
    }

    public void addStringLocalization(String key, String value) {
        this.addStringLocalization(key, "en_US", value);
    }

    public void addStringLocalization(String key, String lang, String value) {
        Properties langPack = (Properties)this.modLanguageData.get(lang);
        if (langPack == null) {
            langPack = new Properties();
            this.modLanguageData.put(lang, langPack);
        }

        langPack.put(key, value);
    }

    public void addStringLocalization(Properties langPackAdditions) {
        this.addStringLocalization(langPackAdditions, "en_US");
    }

    public void addStringLocalization(Properties langPackAdditions, String lang) {
        Properties langPack = (Properties)this.modLanguageData.get(lang);
        if (langPack == null) {
            langPack = new Properties();
            this.modLanguageData.put(lang, langPack);
        }

        if (langPackAdditions != null) {
            langPack.putAll(langPackAdditions);
        }
    }

    public static void reloadLanguageTable() {
        String lang = Language.getInstance().getCode();
        Language.getInstance().code = null;
        Language.getInstance().setCode(lang);
    }

    public void addNameForObject(Object objectToName, String lang, String name) {
        String objectName;
        if (objectToName instanceof Item) {
            objectName = ((Item)objectToName).getTranslationKey();
        } else if (objectToName instanceof Block) {
            objectName = ((Block)objectToName).getTranslationKey();
        } else {
            if (!(objectToName instanceof ItemStack)) {
                throw new IllegalArgumentException(String.format("Illegal object for naming %s", objectToName));
            }

            objectName = ((ItemStack)objectToName).getItem().getTranslationKey((ItemStack)objectToName);
        }

        objectName = objectName + ".name";
        this.addStringLocalization(objectName, lang, name);
    }

    public static void addName(Object objectToName, String name) {
        instance().addNameForObject(objectToName, "en_US", name);
    }

    public void loadLanguageTable(Properties languagePack, String lang) {
        Properties usPack = (Properties)this.modLanguageData.get("en_US");
        if (usPack != null) {
            languagePack.putAll(usPack);
        }

        Properties langPack = (Properties)this.modLanguageData.get(lang);
        if (langPack != null) {
            languagePack.putAll(langPack);
        }
    }

    public void loadLocalization(String localizationFile, String lang, boolean isXML) {
        URL urlResource = this.getClass().getResource(localizationFile);
        if (urlResource != null) {
            this.loadLocalization(urlResource, lang, isXML);
        } else {
            ModContainer activeModContainer = Loader.instance().activeModContainer();
            if (activeModContainer != null) {
                FMLLog.log(
                        activeModContainer.getModId(),
                        Level.SEVERE,
                        "The language resource %s cannot be located on the classpath. This is a programming error.",
                        new Object[]{localizationFile}
                );
            } else {
                FMLLog.log(
                        Level.SEVERE, "The language resource %s cannot be located on the classpath. This is a programming error.", new Object[]{localizationFile}
                );
            }
        }
    }

    public void loadLocalization(URL localizationFile, String lang, boolean isXML) {
        InputStream langStream = null;
        Properties langPack = new Properties();

        try {
            langStream = localizationFile.openStream();
            if (isXML) {
                langPack.loadFromXML(langStream);
            } else {
                langPack.load(new InputStreamReader(langStream, Charsets.UTF_8));
            }

            this.addStringLocalization(langPack, lang);
        } catch (IOException var15) {
            FMLLog.log(Level.SEVERE, var15, "Unable to load localization from file %s", new Object[]{localizationFile});
        } finally {
            try {
                if (langStream != null) {
                    langStream.close();
                }
            } catch (IOException var14) {
            }
        }
    }
}
