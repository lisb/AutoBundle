package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


public class BindingFieldHelper {

    static final Map<TypeName, String> fieldTypes
            = Collections.unmodifiableMap(new HashMap<TypeName, String>(14) {
        {
            put(TypeName.BOOLEAN, "Boolean");
            put(TypeName.BYTE, "Byte");
            put(TypeName.CHAR, "Char");
            put(TypeName.DOUBLE, "Double");
            put(TypeName.FLOAT, "Float");
            put(TypeName.LONG, "Long");
            put(TypeName.INT, "Int");
            put(TypeName.SHORT, "Short");

            put(ClassName.get("java.lang", "CharSequence"), "CharSequence");
            put(ClassName.get("java.lang", "String"), "String");

            put(ClassName.get("android.os", "Bundle"), "Bundle");

            put(ParameterizedTypeName.get(ArrayList.class, CharSequence.class), "CharSequenceArrayList");
            put(ParameterizedTypeName.get(ArrayList.class, Integer.class), "IntegerArrayList");
            put(ParameterizedTypeName.get(ArrayList.class, String.class), "StringArrayList");
        }
    });

    static final Map<TypeName, String> fieldArrayTypes
            = Collections.unmodifiableMap(new HashMap<TypeName, String>(10) {
        {
            put(TypeName.BOOLEAN, "BooleanArray");
            put(TypeName.BYTE, "ByteArray");
            put(TypeName.CHAR, "CharArray");
            put(TypeName.DOUBLE, "DoubleArray");
            put(TypeName.FLOAT, "FloatArray");
            put(TypeName.LONG, "LongArray");
            put(TypeName.INT, "IntArray");
            put(TypeName.SHORT, "ShortArray");
            put(ClassName.get("java.lang", "CharSequence"), "CharSequenceArray");
            put(ClassName.get("java.lang", "String"), "StringArray");
        }
    });

    static String getOperationName(TypeName target, Elements elements, Types types) {
        if (fieldTypes.containsKey(target)) {
            return fieldTypes.get(target);
        }

        // Array
        TypeMirror parcelable = elements.getTypeElement("android.os.Parcelable").asType();
        if (target.toString().endsWith("[]")) {
            String removed = target.toString().substring(0, target.toString().length() - 2);
            for (TypeName arrayType : fieldArrayTypes.keySet()) {
                if (removed.equals(arrayType.toString())) {
                    return fieldArrayTypes.get(arrayType);
                }
            }
            // Parcelable[]
            TypeElement element = elements.getTypeElement(removed);
            if (element != null && types.isAssignable(element.asType(), parcelable)) {
                return "ParcelableArray";
            }
        }

        TypeMirror targetType = getTypeMirror(target, elements, types);

        // Parcelable
        if (types.isAssignable(targetType, parcelable)) {
            return "Parcelable";
        }

        // ArrayList<? extend Parcelable>
        TypeElement arrayList = elements.getTypeElement(ArrayList.class.getName());
        TypeMirror wildCardParcelable = types.getWildcardType(parcelable, null);
        TypeMirror parcelableArrayList = types.getDeclaredType(arrayList, wildCardParcelable);
        if (types.isAssignable(targetType, parcelableArrayList)) {
            return "ParcelableArrayList";
        }

        // SparseArray<? extend Parcelable>
        TypeElement sparseArray = elements.getTypeElement("android.util.SparseArray");
        TypeMirror sparceParcelableArray = types.getDeclaredType(sparseArray, wildCardParcelable);
        if (types.isAssignable(targetType, sparceParcelableArray)) {
            return "SparseParcelableArray";
        }

        // IBinder
        TypeMirror iBinder = elements.getTypeElement("android.os.IBinder").asType();
        if (types.isAssignable(targetType, iBinder)) {
            return "Binder";
        }

        // Serializable
        TypeMirror serializable = elements.getTypeElement(Serializable.class.getName()).asType();
        if (types.isAssignable(targetType, serializable)) {
            return "Serializable";
        }

        return null;
    }

    static TypeMirror getTypeMirror(TypeName target, Elements elements, Types types) {
        if (target instanceof ParameterizedTypeName) {
            final ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) target;
            final TypeElement genericType = elements.getTypeElement(
                    parameterizedTypeName.rawType.toString());
            final List<TypeName> typeArguments = parameterizedTypeName.typeArguments;
            final TypeMirror[] argTypes = new TypeMirror[typeArguments.size()];
            for (int i = 0; i < argTypes.length; i++) {
                final TypeName typeArgument = typeArguments.get(i);
                argTypes[i] = getTypeMirror(typeArgument, elements, types);
            }
            return types.getDeclaredType(genericType, argTypes);
        } else {
            return elements.getTypeElement(target.toString()).asType();
        }
    }

}
