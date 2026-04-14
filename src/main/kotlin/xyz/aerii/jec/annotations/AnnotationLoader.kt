package xyz.aerii.jec.annotations

import io.github.classgraph.ClassGraph
import xyz.aerii.library.utils.safely

object AnnotationLoader {
    fun load() {
        ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .acceptPackages("xyz.aerii.jec")
            .scan()
            .use { scan ->
                scan
                    .getClassesWithAnnotation(Load::class.java.name)
                    .loadClasses()
                    .forEach { safely { Class.forName(it.name) } }
            }
    }
}