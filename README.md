# SnowcoalsTools

This is a plugin made by me (snowcoal) that has various building tools that I wanted and then made. It requires FastAsyncWorldEdit installed on the server.
The various features are listed below:

    Java version of CityGenerator
    //waterrode command that does a water erosion simulation on terrain
    //smoothstairslab command that smooths terrain into stairs and slabs. Works with overhangs/caves/etc
    //dim3smooth command that does a 3 dimensional terrain Gaussian smoothing operation. Works with overhangs/caves/etc

This plugin works in 1.18 - 1.20.1, however the //smoothstairslab command only works in certain versions

# CityGenerator Command

This is a tool that generates cities using a maze-based algorithm.

Most of the code for this was directly copied from my C++ CityGenerator project, and some conversions were done to fix syntax stuff.
Thus, there are often comments or variable names referring to pointers, even though Java does not have pointers. There might also be comments
of old C++ code, although I have tried to remove most of these.

The original project can be found here: https://github.com/snowcoal/CityGenerator

There are two main commands for this tool, ```/houseset``` and ```/citygen```. ```/houseset``` creates the set of houses that will be used to generate the city.
To create a new houseset, make a cuboid selection of the house, and do ```/houseset addhouse <type>```, while standing in the middle of the house. 
These are the possible values of the type field:

        exactly 2 neighbors, one on each side: -2
                                  0 neighbors: 0
                1 neighbor on the bottom side: 1
                     2 neighbors in "L" shape: 2
                     3 neighbors in "T" shape: 3
                                  4 neighbors: 4

Houses of type -2 can be any odd width, as long is theres a cooresponding house that adds to the total width of 2 houses - 1. All houses of type 0-4 must have
a set odd width, which is specified later when generating the city.

Note that there must be at least one of each type of house in the set, or generaton will fail. Additionally, any house of type -2 that is not the standard
width must have a cooresponding house such that their combined widths is twice the standard width minus 1.

to save or load a houseset, do ```/houseset save <name>``` or ```/houseset load <name>```
```/houseset list``` can also be used to view all housesets saved
```/houseset clear``` clears the currently loaded houseset

Note that all housesets are .csv files and can be edited manually if desired.

The second main command is ```/citygen```, which has sub-commands ```/citygen genbase <House Width> <Left-Right Bias> <Road Dist>``` and ```/citygen gencity```

House Width is the standard width of all the houses, and must be an odd integer.
Left-Right bias is an integer from 0-100 and changes the layout of the maze used for the city. Set it to 50 for a "normal" maze.
Road Dist is an integer from 0-100 and is the percentage of times a road is added. These roads are randomly added to the maze to "break it up".

To run ```/citygen genbase```, first make a 2D polygonal selection of where the city is to be generated. Then stand at the desired height and run the command.

After the base of the city is generated, the houses can then be added and the city pasted in. A houseset must be loaded before this can take place. To paste
the final city, run ```/citygen gencity``` which will paste it at the height of the player

# WaterErode Command

This command does a water erosion simulation on the terrian and erodes it. This is simply done with the ```//watererode``` or ```//werode``` command on a selected area.
This will add channels into the terrain. A smoothing operation is automatically done as well to clean up noise.

# SmoothStairSlab Command

THIS COMMAND IS ONLY ENABLED IN THE FOLLOWING VERSIONS: 1.18.1, 1.19.3, 1.20.1
This is due to how I place stairs and slabs with specific orientations using FAWE

This command smooths terrain into stairs and slabs. The idea is similar to the FAWE //smoothsnow command, however the implementation is
completely different. To run, do ```//smoothstairslab``` or ```//smss``` after making a selection. It works upside down and right side up, in caves, regardless
of how many overhangs there might be. Basically theres zero restrictions for where it will work and where it wont (However note that it can have noisy outputs if
you attempt to smooth 1-block thick surfaces).

Its important to note that this command works best when the terrain is already somewhat smooth.
This can be easily accomplished by running the ```//dim3smooth``` command prior to running this command.

This command is not fully deterministic, and is optimized to produce the "best-looking" output that I could find. The algorithm it does is quite complex and
very dependent on the shape of the terrain. Thus, it has a varying running time, as it sometimes simply does some logical checks to determine the output block,
and other times it does a 3D guassian smoothing convolution operation prior to placing some of the slabs. The approximate running time could be determined,
but again, this is heavily dependent on the shape of the terrain. More "flat" areas typically means more convolutions and thus a slightly longer running time.

# Dim3Smooth Command

This command does a 3-Dimensional Guassian blur/smooth on terrain. It is functionally similar to the FAWE //smooth command.
However, the difference is that this command works with caves, overhangs, cliffs, and even large trees. It doesnt matter what the shape of the terrain is, it will smooth it.
The command is ```//dim3smooth <FilterSize> <NumIterations> <Cutoff>```.

The first parameter filterSize is the size of the smoothing filter.
The filter size determines how "blurred" the output is. A larger filter means that the output will have less rough corners/edges, and a smaller filter means it will have more.
This needs to be an odd number greater or equal to 3. The larger the number, the larger the area is sampled for the smoothing. Higher numbers can significantly increase runtime.

The second parameter numIterations is optional, and the default is 1. This parameter sets how many smoothing operations are done. For example, running "//dim3smooth 3 2"
is equivlant to running "//dim3smooth 3" twice in a row.

The third parameter cutoff is also optional and more advanced. This parameter sets the cutoff value for when to place blocks vs air. The default is 0.5.
Setting it higher will remove more blocks and thus make objects become smaller, and will make caves wider. Setting it lower will add blocks, thus making objects larger,
and making caves thinner. It is important to note that for every block that is added, a search must be done to determine which block is the closest to the one being added.
This search is limited to 100 toal blocks for performance reasons, and also makes the runtime slower as more blocks are searched.

The run time of this command is O(n * (k^3 + s)). n is the number of blocks in the selection, k is FilterSize, and s is the average number of blocks that need
to be searched to add a new one (only relevant when new blocks are being added). For best performance, k should be set low (eg. 3-5),
since increasing it causes significantly longer run times with higher k.

# Disclaimer

This plugin was created by me for my personal singleplayer use. Becuase of that, this plugin was NOT designed to be used in multiplayer environments or with untrusted players.
For example, there are no permissions. There are also often no bounds checks on selection sizes, as I like to test the plugin to its fullest. Therefore, it can and will
crash your server if used improperly. Take backups and do not use this plugin on a public server.

Because of all this, there are currently no public releases of this plugin, and there probably won't ever be. To install this plugin, you must first compile it into a .jar.
There is a pom.xml file included to help with this. Note that a 1.18+ release of FastAsyncWorldEdit must be also be linked as a library to the plugin prior to compilation.

# Future Enchancements

    Command to place structures around a shell without going inside it
    Thermal erosion
    //wall command to automatically generate stone walls for farms
    hexagonal city generator
    Add ability to remove specific houses from houseset, and view houses in houseset
    Add permissions support
    Add support for multiple players running commands at once
    Extend city size

# Contributors

    Omega - updated //smoothstairslab command to 1.20.1 and wrote new generator script for API changes

# Notable Code References

    Big thanks to Job Talle's blog for the erosion simulation algorithm:
        https://jobtalle.com/simulating_hydraulic_erosion.html

    Most Valuable Stack Overflow Posts:
        https://stackoverflow.com/questions/5281261/generating-a-normal-map-from-a-height-map

    I would be nowhere without the FAWE github:
        https://github.com/IntellectualSites/FastAsyncWorldEdit

