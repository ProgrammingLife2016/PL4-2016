# PL4-2016

Travis CI Build status: [![](https://travis-ci.org/ProgrammingLife2016/PL4-2016.svg?branch=master)](https://travis-ci.org/ProgrammingLife2016/PL4-2016)

## Group Information
**Group name:** DART-N
**Group members:** Arthur Breurkes, Ricardo Jongerius, Niek van der Laan, Daphne van Tetering, Niels Warnars, Ties Westerborg

## Build information

### Required packages

 - Git 
 - Maven and 
 - JDK8

#### Ubuntu

    apt-get update
    apt-get install git maven openjdk-8-jdk

#### Windows & OS X

Run through IDE, such as [IntelliJ IDEA](https://www.jetbrains.com/idea/).

### Executing the application

    git clone https://github.com/ProgrammingLife2016/PL4-2016.git
    cd PL4-2016
    mvn install
    java -jar target/Contex-0.8.jar

*Note:* File name may change along with current application version. Please mind the version number for running the .jar file.

## Using the application
We will explain the usage of the program step by step. The same information can be found in the application itself under "_help -> about_". Any area with a scrollbar can be scrolled through using the mouse wheel or the trackpad.

### Starting off
After the application has loaded, load required files (.gfa, .nwk, .gff, .xlsx) via the "_file_" menu. The application will now automatically visualize the Phylogenetic tree. **NOTE:** all resource files must be located in the same directory.

### The Phylogenetic tree
Once in the Phylogenetic tree, you can explore the tree by scrolling up and down. When hovering over one of the tree leaves, you'll see all available meta data for that specific genome (a leaf represents a genome) in the info box on the right of the screen. In the upper part of the screen are _"select all"_ and "_deselect all_" buttons to select and deselect all genomes respectively.

#### Selecting genomes
You can select genomes, simply by clicking the corresponding leaf of nearest edge. If you'd desire to select a group of genomes at once, you can select them by clicking the edge that groups all of them together. Edge and leaf highlighting will be applied on hovering over and on selection. After making a selection, you can view the selected genomes in the graph by selecting "_view -> show selected strains in graph_".
#### Applying filters
If you would want to select genomes based on their meta data, for example their lineage, you can do this by using the "_filter_" menu. In categories, filters work as logical or predicates. In between categories, filters work as logical and filters. Please note that manual selection is no longer a possibility after applying filters. All manual selection will be undone on applying a filter. After making a selection, you can view the selected genomes in the graph by selecting "_view -> show selected strains in graph_".
#### Searching in the tree
Say you want to select a specific genome such as "TKK_02_0001", but you cannot find it in the tree. You can type the name of the desired genome in the text box in the upper part of the screen. After entering its name, you can press the search button and the genome will be automatically selected. After making a selection, you can view the selected genomes in the graph by selecting "_view -> show selected strains in graph_".

### The Graph
Once in the graph, you will see all in the three selected strains visualized as a graph. In the bottom part of the screen, you will see the legend for nodes and edges in the graph and a zoom box. This zoom box indicates how far zoomed in you are and where you are in the graph in terms of x-coordinate. At the right part of the screen, you will see a list with loaded genomes and an information box. This box of information will show which filters are active and contain information on selected nodes. You can select nodes by clicking on them. From here, you can also select filters, would you so desire.
####Selecting nodes
You can select nodes by clicking on the desired node. Once selected, this node will remain in focus. To remove focus on a node, you can simply click it again to deselect it. While a node is selected, horizontal zooming is disabled.
####Highlighting a strain
Strain highlighting can be applied on one or multiple strains. You can do this by selecting the strain(s) you wish to highlight in the list of genomes on the right part of the screen. If you desire to select multiple strains you can use the Shift Click/control Click as you normally would in 
####Nucleotide level
For load time enhancement, the nucleotide level is not rendered by default. However, you can reach it by allowing this level to be rendered. You can do so by selecting "_view -> allow nucleotide level_".
####Annotation search
In the topmost part of the screen, you can search for annotation information (such as "GyrA"). This highlighting will bring you to nucleotide zoom level, where the nodes that contain the sought for annotation will be highlighted with a yellow outline color.
####Reset view
The graph view can be reset by selecting "_view -> reset_". This will not reset your filters or selection in the phylogenetic tree.
