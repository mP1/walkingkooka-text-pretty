[![Build Status](https://travis-ci.com/mP1/walkingkooka-text-pretty.svg?branch=master)](https://travis-ci.com/mP1/walkingkooka-text-pretty.svg?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-text-pretty/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-text-pretty?branch=master)

A collection of utilties to help format and beautify printed text into tables with column alignment formatting and more.


# TreePrinting Sample

The sample below gives an example to print to the console many paths in a tidy, tree with indentation without overwhelming
the user with very long path names.

```java
   /**
     * Prints...
     * <pre>
     * ~/github/project
     *   target
     *     classes/java/walkingkooka/text/pretty
     *       CharSequenceBiFunction.class
     *       CharSequenceBiFunctionAlign.class
     *     maven-archiver
     *       pom.properties
     *     maven-status/maven-compiler-plugin
     *       compile/default-compile
     *         createdFiles.lst
     *         inputFiles.lst
     *       testCompile/default-compile
     *         createdFiles.lst
     *         inputFiles.lst
     *   jacoco.exec
     *   walkingkooka-text-pretty-1.0-SNAPSHOT-sources.jar
     *   walkingkooka-text-pretty-1.0-SNAPSHOT.jar
     * </pre>
     */
    public static void main(final String[] ignored) {
        final IndentingPrinter printer = Printers.sysOut().indenting(Indentation.with("  "));

        // over simplified sample of this projects target directory.
        final Set<StringPath> paths = Sets.of(
                "/target/classes/java/walkingkooka/text/pretty/CharSequenceBiFunction.class",
                "/target/classes/java/walkingkooka/text/pretty/CharSequenceBiFunctionAlign.class", // some class files...
                "/target/maven-archiver/pom.properties",
                "/target/maven-status/maven-compiler-plugin/compile/default-compile/createdFiles.lst",
                "/target/maven-status/maven-compiler-plugin/compile/default-compile/inputFiles.lst",
                "/target/maven-status/maven-compiler-plugin/testCompile/default-compile/createdFiles.lst",
                "/target/maven-status/maven-compiler-plugin/testCompile/default-compile/inputFiles.lst",
                "/jacoco.exec",
                "/walkingkooka-text-pretty-1.0-SNAPSHOT.jar",
                "/walkingkooka-text-pretty-1.0-SNAPSHOT-sources.jar")
                .stream()
                .map(s -> StringPath.parse("/~/github/project" + s))
                .collect(Collectors.toSet());

        new TreePrinting<StringPath, StringName>() {

            @Override
            public void branchBegin(final List<StringName> names, final IndentingPrinter printer) {
                final String path = this.toPath(names);
                if (false == path.isEmpty()) {
                    printer.print(path + "\n");
                    printer.indent();
                }
            }

            @Override
            public void branchEnd(final List<StringName> names, final IndentingPrinter printer) {
                final String path = this.toPath(names);
                if (false == path.isEmpty()) {
                    printer.outdent();
                }
            }

            private String toPath(final List<StringName> names) {
                return names.stream()
                        .filter(n -> false == n.value().isEmpty())
                        .map(StringName::toString)
                        .collect(Collectors.joining("/"));
            }

            @Override
            public void children(final Set<StringPath> paths, final IndentingPrinter printer) {
                paths.forEach(p -> printer.print(p.name() + "\n"));
            }
        }.biConsumer()
                .accept(paths, printer);
    }
```
