package foo.starred.jec.annotations

import foo.starred.snowbird.utils.safely
import io.github.classgraph.ClassGraph

object AnnotationLoader {
    fun load() {
        ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .acceptPackages("foo.starred.jec")
            .scan()
            .use { scan ->
                scan
                    .getClassesWithAnnotation(Load::class.java.name)
                    .loadClasses()
                    .forEach { safely { Class.forName(it.name) } }
            }
    }
}