package me.ryguy.paperanno;

import me.ryguy.paperanno.anno.Bootstrapper;
import me.ryguy.paperanno.anno.Loader;
import me.ryguy.paperanno.anno.PaperPlugin;
import me.ryguy.paperanno.anno.dependency.Dependency;
import me.ryguy.paperanno.anno.load.LoadAfter;
import me.ryguy.paperanno.anno.load.LoadBefore;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;

//TODO: Fix serialization return logic to remove need for absurd exception check at the end
@SupportedAnnotationTypes({"me.ryguy.paperanno.anno.PaperPlugin", "me.ryguy.paperanno.anno.Loader",
        "me.ryguy.paperanno.anno.Bootstrapper"})
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class AnnotationProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Map<String, Object> resultant = new LinkedHashMap<>(); //retain order for readability
        //processing PaperPlugin
        TypeElement pluginClass = getSingleClassBoundAnno(roundEnvironment, PaperPlugin.class,
                "org.bukkit.plugin.java.JavaPlugin", false);
        if(pluginClass != null) {
            PaperPlugin pluginAnno = pluginClass.getAnnotation(PaperPlugin.class);
            resultant.put("name", pluginAnno.name());
            resultant.put("version", pluginAnno.version());
            resultant.put("main", pluginClass.getQualifiedName().toString());
            resultant.put("description", pluginAnno.desc());
            resultant.put("api-version", pluginAnno.apiVersion());
            resultant.put("has-open-classloader", pluginAnno.hasOpenClassLoader());
            //processing dependencies
            Dependency[] dependencies = pluginClass.getAnnotationsByType(Dependency.class);
            if (dependencies.length != 0) {
                List<Map<String, Object>> obj = new ArrayList<>();
                for(Dependency anno : dependencies) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("name", anno.name());
                    map.put("required", anno.required());
                    map.put("bootstrap", anno.bootstrap());
                    obj.add(map);
                }
                resultant.put("dependencies", obj);
            }
            //processing loading
            LoadBefore.Plugin[] loadBefore = pluginClass.getAnnotationsByType(LoadBefore.Plugin.class);
            if (loadBefore.length != 0) {
                List<Map<String, Object>> obj = new ArrayList<>();
                for(LoadBefore.Plugin anno : loadBefore) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("name", anno.name());
                    map.put("bootstrap", anno.bootstrap());
                    obj.add(map);
                }
                resultant.put("load-before", obj);
            }
            LoadAfter.Plugin[] loadAfter = pluginClass.getAnnotationsByType(LoadAfter.Plugin.class);
            if (loadAfter.length != 0) {
                List<Map<String, Object>> obj = new ArrayList<>();
                for(LoadAfter.Plugin anno : loadAfter) {
                    Map<String, Object> map = new LinkedHashMap<>(); //retain order for readability
                    map.put("name", anno.name());
                    map.put("bootstrap", anno.bootstrap());
                    obj.add(map);
                }
                resultant.put("load-after-before", obj);
            }
        }
        //processing bootstrapper
        TypeElement bootstrapper = getSingleClassBoundAnno(roundEnvironment, Bootstrapper.class,
                "io.papermc.paper.plugin.bootstrap.PluginBootstrap", true);
        if (bootstrapper != null) {
            resultant.put("bootstrapper", bootstrapper.getQualifiedName().toString());
        }
        //processing loader
        TypeElement loader = getSingleClassBoundAnno(roundEnvironment, Loader.class,
                "io.papermc.paper.plugin.loader.PluginLoader", true);
        if (loader != null) {
            resultant.put("loader", loader.getQualifiedName().toString());
        }
        try {
            serialize(resultant);
        } catch (IOException ex) {
            if(ex instanceof FilerException && ex.getMessage().toLowerCase().contains("to reopen a file for path"))
                return true; //this is the laziest fix i've ever implemented
            error("Failed to create plugin.yml file!");
            throw new RuntimeException(ex);
        }
        return true;
    }

    private void serialize(Map<String, Object> yml) throws IOException {
        FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "paper-plugin.yml");
        try (Writer writer = file.openWriter()) { //uses closeable, try with closes it
            String raw = new Yaml().dumpAs(yml, Tag.MAP, DumperOptions.FlowStyle.BLOCK);
            writer.write(raw);
            writer.flush();
            writer.close();
        }
    }

    private TypeElement getSingleClassBoundAnno(RoundEnvironment env, Class<? extends Annotation> annotationClass,
                                                String inheritedClass, boolean isInterface) {
        List<TypeElement> types = env.getElementsAnnotatedWith(annotationClass).stream()
                .map(e -> (TypeElement) e) //no need to check, annotations have relevant target annotation
                .filter(e -> {
                    if(isInterface)
                        return e.getInterfaces().stream().map(TypeMirror::toString).anyMatch(i -> i.equals(inheritedClass));
                    else
                        return e.getSuperclass().toString().equals(inheritedClass);
                })
                .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
                .filter(e -> !e.getModifiers().contains(Modifier.ABSTRACT))
                .distinct().toList();
        if (types.size() != 1) {
            return null;
        } else {
            return types.get(0);
        }
    }

    private void error(String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, msg);
    }

}
