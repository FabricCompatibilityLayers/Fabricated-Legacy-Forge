package fr.catcore.fabricatedforge.forged;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;
import net.fabricmc.loader.api.metadata.Person;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class FabricModContainer implements ModContainer {
    private final net.fabricmc.loader.api.ModContainer container;
    private final ModMetadata modMetadata = new ModMetadata();

    public FabricModContainer(net.fabricmc.loader.api.ModContainer container) {
        this.container = container;

        List<String> authors = new ArrayList<>();

        for (Person person : this.container.getMetadata().getAuthors()) {
            authors.add(person.getName());
        }

        this.modMetadata.authorList = authors;

        this.modMetadata.url = this.container.getMetadata().getContact().get("url").orElse("");
        this.modMetadata.description = this.container.getMetadata().getDescription();
        this.modMetadata.name = this.container.getMetadata().getName();
        Optional<String> iconPathway = this.container.getMetadata().getIconPath(1);

        if (iconPathway.isPresent()) {
            Optional<Path> iconPath = this.container.findPath("/" + iconPathway.get());

            if (iconPath.isPresent()) {
                this.modMetadata.logoFile = "/" + iconPathway.get();
            }
        }
    }

    @Override
    public String getModId() {
        return this.container.getMetadata().getId();
    }

    @Override
    public String getName() {
        return this.container.getMetadata().getName();
    }

    @Override
    public String getVersion() {
        return this.container.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public File getSource() {
        return this.container.getRootPaths().get(0).toFile();
    }

    @Override
    public ModMetadata getMetadata() {
        return this.modMetadata;
    }

    @Override
    public void bindMetadata(MetadataCollection metadataCollection) {

    }

    @Override
    public void setEnabledState(boolean bl) {

    }

    @Override
    public Set<ArtifactVersion> getRequirements() {
        return new HashSet<>();
    }

    @Override
    public List<ArtifactVersion> getDependencies() {
        return new ArrayList<>();
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        return new ArrayList<>();
    }

    @Override
    public String getSortingRules() {
        return "";
    }

    @Override
    public boolean registerBus(EventBus eventBus, LoadController loadController) {
        return false;
    }

    @Override
    public boolean matches(Object object) {
        return false;
    }

    @Override
    public Object getMod() {
        return null;
    }

    @Override
    public ArtifactVersion getProcessedVersion() {
        return null;
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public boolean isNetworkMod() {
        return true;
    }

    @Override
    public String getDisplayVersion() {
        return this.container.getMetadata().getVersion().getFriendlyString();
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return null;
    }
}
