package com.yatatsu.autobundle.processor;

import com.google.auto.service.AutoService;
import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class AutoBundleProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        final Set<String> supportedAnnotationTypes = new HashSet<>();
        supportedAnnotationTypes.add(AutoBundleType.class.getCanonicalName());
        supportedAnnotationTypes.add(AutoBundleField.class.getCanonicalName());
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<AutoBundleBindingClass> classes =
                new ArrayList<>(BindingDetector.bindingClasses(roundEnv, elementUtils, typeUtils));
        if (!classes.isEmpty()) {
            try {
                AutoBundleBinderWriter binderWriter = new AutoBundleBinderWriter(classes);
                binderWriter.write(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        for (AutoBundleBindingClass clazz : classes) {
            AutoBundleWriter writer = new AutoBundleWriter(clazz);
            try {
                writer.write(filer);
            } catch (IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        }
        return true;
    }
}
