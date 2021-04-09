[![Build Status](https://github.com/mP1/walkingkooka-text-pretty/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-text-pretty/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-text-pretty/badge.svg?branch=master)](https://coveralls.io/github/mP1/walkingkooka-text-pretty?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-text-pretty.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-text-pretty/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-text-pretty.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-text-pretty/alerts/)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)



A collection of utilties to help format and beautify printed text into tables with column alignment formatting and more.


# TreePrinting Sample

The sample below gives an example to print to the console many paths in a tidy, tree with indentation without overwhelming
the user with very long path names.

```java
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
        return this.toPath(names, StringPath.separator());
    }

    @Override
    public void children(final Set<StringPath> paths, final IndentingPrinter printer) {
        paths.forEach(p -> printer.print(p.name() + "\n"));
    }
}.biConsumer()
        .accept(paths, printer);
```

prints the directory tree, notice the indentation and grouping of directory paths where possible for a compact yet
indented tree.

```text
~/github/project
  target
    classes/java/walkingkooka/text/pretty
      CharSequenceBiFunction.class
      CharSequenceBiFunctionAlign.class
    maven-archiver
      pom.properties
    maven-status/maven-compiler-plugin
      compile/default-compile
        createdFiles.lst
        inputFiles.lst
      testCompile/default-compile
        createdFiles.lst
        inputFiles.lst
  jacoco.exec
  walkingkooka-text-pretty-1.0-SNAPSHOT-sources.jar
  walkingkooka-text-pretty-1.0-SNAPSHOT.jar
```



# Table, Column, Print

The sample below
```java
// create three columns with different widths and alignments.
final ColumnConfig states = TextPretty.columnConfig()
        .minWidth(20)
        .maxWidth(20)
        .leftAlign();

final ColumnConfig population = TextPretty.columnConfig()
        .minWidth(10)
        .maxWidth(10)
        .rightAlign();

final ColumnConfig money = TextPretty.columnConfig()
        .minWidth(12)
        .maxWidth(12)
        .characterAlign(CharPredicates.is('.'), 7);

// populate table with 3 columns.
final TableConfig tableConfig = TextPretty.tableConfig()
        .add(states)
        .add(population)
        .add(money);

// create table with a single row from a csv line
final Table table1 = TextPretty.table()
        .setRow(0, TextPretty.csv(',').apply("\"New South Wales\",10000000,$12.00"));

// streaming a list of csv lines (different delimiters) and collect (aka add to table)
final Table table123 = Lists.of(TextPretty.csv('/').apply("Queensland/4000000/$11.75"),
        TextPretty.csv(';').apply("Tasmania;500000;$9.0"))
        .stream()
        .collect(table1.collectRow(1));

// format the table with cells using the columns.
final Table formattedTable = tableConfig.apply(table123);

// print row by row
try (final IndentingPrinter printer = Printers.sysOut().indenting(Indentation.with("  "))) {
    for (int i = 0; i < formattedTable.maxRow(); i++) {
        printer.print(TextPretty.rowColumnsToLine((column -> 2), LineEnding.SYSTEM)
                .apply(formattedTable.row(i)));
    }
}
```

prints 3 columns, the first is left aligned, the second is right aligned, and the third and last is centered on the decimal 
point.

```text
New South Wales         10000000      $12.00
Queensland               4000000      $11.75
Tasmania                  500000       $9.0
```
