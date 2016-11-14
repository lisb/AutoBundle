package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import com.yatatsu.autobundle.AutoBundleConverter;
import com.yatatsu.autobundle.AutoBundleField;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


public class AutoBundleBindingField {

    private final String fieldName;
    private final String argKey;
    private final boolean required;
    private final TypeName argType;
    private final TypeName converter;
    private final boolean hasCustomConverter;
    private final String operationName;
    private final boolean generateBuildParam;
    private final boolean generateGetter;
    private String getterName;
    private String setterName;

    public AutoBundleBindingField(VariableElement element,
                                  AutoBundleField annotation,
                                  Elements elementUtils,
                                  Types typeUtils,
                                  String getterName,
                                  String setterName) {
        this.fieldName = element.toString();
        this.argKey = annotation.key().length() > 0 ? annotation.key() : this.fieldName;
        this.generateBuildParam = annotation.generateBuilderParam();
        this.generateGetter = annotation.generateGetter();
        this.required = generateBuildParam && annotation.required();
        this.argType = TypeName.get(element.asType());
        this.getterName = getterName;
        this.setterName = setterName;
        Validator.checkAutoBundleFieldModifier(element, hasGetter() && hasSetter());

        TypeName converter;
        TypeName converted;
        try {
            Class clazz = annotation.converter();
            Validator.checkConverterClass(clazz);
            converter = TypeName.get(clazz);
            converted = detectConvertedTypeNameByClass(clazz);
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            Validator.checkConverterClass(classTypeElement);
            converter = TypeName.get(classTypeMirror);
            converted = detectConvertedTypeByTypeElement(classTypeElement);
        }
        this.converter = converter;
        this.hasCustomConverter =
                !this.converter.equals(ClassName.get("com.yatatsu.autobundle", "DefaultAutoBundleConverter"));
        if (hasCustomConverter) {
            operationName = BindingFieldHelper.getOperationName(converted, elementUtils, typeUtils);
        } else {
            operationName = BindingFieldHelper.getOperationName(argType, elementUtils, typeUtils);
        }

        if (hasCustomConverter) {
            Validator.checkNotSupportedOperation(operationName, converted);
        } else {
            Validator.checkNotSupportedOperation(operationName, argType);
        }
    }

    public TypeName getArgType() {
        return argType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getArgKey() {
        return argKey;
    }

    public boolean isRequired() {
        return required;
    }

    public TypeName getConverter() {
        return converter;
    }

    public boolean hasCustomConverter() {
        return hasCustomConverter;
    }

    public String getOperationName(String operation) {
        return operation + operationName;
    }

    public String getGetterName() {
        return getterName;
    }

    public boolean hasGetter() {
        return getterName != null && getterName.length() > 0;
    }

    public String getSetterName() {
        return setterName;
    }

    public boolean hasSetter() {
        return setterName != null && setterName.length() > 0;
    }

    public boolean isGenerateBuildParam() {
        return generateBuildParam;
    }

    public boolean isGenerateGetter() {
        return generateGetter;
    }

    public boolean noCast() {
        return operationName.equals("ParcelableArrayList") ||
                operationName.equals("ParcelableArray") ||
                operationName.equals("SparseParcelableArray");
    }

    static TypeName detectConvertedTypeByTypeElement(TypeElement element) {
        TypeMirror typeMirror = getConverterGenericsTypesByTypeElement(element).get(1);
        TypeName typeName = TypeName.get(typeMirror);
        try {
            typeName = typeName.unbox();
        } catch (UnsupportedOperationException ignore) {}
        return typeName;
    }

    static List<? extends TypeMirror> getConverterGenericsTypesByTypeElement(TypeElement element) {
        TypeElement targetType = element;
        while (targetType != null) {
            if (!targetType.getInterfaces().isEmpty()) {
                for (TypeMirror in : targetType.getInterfaces()) {
                    DeclaredType inType = (DeclaredType) in;
                    if (inType.asElement().getSimpleName().toString()
                            .equals(AutoBundleConverter.class.getSimpleName())) {
                        return inType.getTypeArguments();
                    }
                }
            }

            TypeMirror superClassType = targetType.getSuperclass();
            if (superClassType.getKind().equals(TypeKind.DECLARED)) {
                targetType = (TypeElement) ((DeclaredType) superClassType).asElement();
            } else {
                break;
            }
        }
        throw new UnsupportedOperationException("Not found convert type: " + element.toString());
    }

    static TypeName detectConvertedTypeNameByClass(Class clazz) {
        Type converted = getConverterGenericsTypesByClass(clazz)[1];
        TypeName typeName = TypeName.get(converted);
        try {
            typeName = typeName.unbox();
        } catch (UnsupportedOperationException ignore) {}
        return typeName;
    }

    static Type[] getConverterGenericsTypesByClass(Class clazz) {
        Type type = AutoBundleConverter.class;
        Class targetClass = clazz;
        while (targetClass != null) {
            for (Type in : targetClass.getGenericInterfaces()) {
                if (in instanceof ParameterizedType &&
                        type.equals(((ParameterizedType) in).getRawType())) {
                    return ((ParameterizedType)in).getActualTypeArguments();
                }
            }
            Type superClazz = targetClass.getGenericSuperclass();
            if (superClazz instanceof  ParameterizedType &&
                    type.equals(((ParameterizedType) superClazz).getRawType())) {
                return ((ParameterizedType) superClazz).getActualTypeArguments();
            }
            targetClass = targetClass.getSuperclass();
        }
        throw new UnsupportedOperationException("Not found convert type: " + clazz.getName());
    }
}
