package cpw.mods.fml.common.discovery.asm;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.Map;

public class ModAnnotation {
    ASMModParser.AnnotationType type;
    Type asmType;
    String member;
    Map<String, Object> values = Maps.newHashMap();
    private ArrayList<Object> arrayList;
    private Object array;
    private String arrayName;
    private ModAnnotation parent;

    public ModAnnotation(ASMModParser.AnnotationType type, Type asmType, String member) {
        this.type = type;
        this.asmType = asmType;
        this.member = member;
    }

    public ModAnnotation(ASMModParser.AnnotationType type, Type asmType, ModAnnotation parent) {
        this.type = type;
        this.asmType = asmType;
        this.parent = parent;
    }

    public String toString() {
        return Objects.toStringHelper("Annotation")
                .add("type", this.type)
                .add("name", this.asmType.getClassName())
                .add("member", this.member)
                .add("values", this.values)
                .toString();
    }

    public ASMModParser.AnnotationType getType() {
        return this.type;
    }

    public Type getASMType() {
        return this.asmType;
    }

    public String getMember() {
        return this.member;
    }

    public Map<String, Object> getValues() {
        return this.values;
    }

    public void addArray(String name) {
        this.arrayList = Lists.newArrayList();
        this.arrayName = name;
    }

    public void addProperty(String key, Object value) {
        if (this.arrayList != null) {
            this.arrayList.add(value);
        } else {
            this.values.put(key, value);
        }
    }

    public void addEnumProperty(String key, String enumName, String value) {
        this.values.put(key, new ModAnnotation.EnumHolder(enumName, value));
    }

    public void endArray() {
        this.values.put(this.arrayName, this.arrayList);
        this.arrayList = null;
    }

    public ModAnnotation addChildAnnotation(String name, String desc) {
        return new ModAnnotation(ASMModParser.AnnotationType.SUBTYPE, Type.getType(desc), this);
    }

    public class EnumHolder {
        private String desc;
        private String value;

        public EnumHolder(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }
    }
}
