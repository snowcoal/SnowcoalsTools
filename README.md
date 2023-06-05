# SnowcoalsTools

This is a plugin made by me (snowcoal) that has various building tools that I wanted and then made. It uses the FastAsyncWorldEdit API and thus requires it installed on the server.
The various features are listed below:

    Java version of CityGenerator
    FAWE command that does a water erosion simulation on terrain
    FAWE command that smooths terrain into stairs and slabs. Works with overhangs/caves/etc
    FAWE command that does a 3 dimensional terrain smoothing operation. Works with overhangs/caves/etc

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
This will add channels into the terrain. A smoothing operation is done as well to clean up noise.

# SmoothStairSlab Command

This command smooths terrain into stairs and slabs. The idea is similar to the FAWE //smoothsnow command, however the implementation is
completely different. To run, do ```//smoothstairslab``` or ```//smss``` after making a selection. It works upside down and right side up, in caves, regardless
of how many overhangs there might be. Basically theres zero restrictions for where it will work and where it wont (However note that it will likely fail if
you attempt to smooth a 1-block thick surface). Also, it works best when the terrain is already somewhat smooth, as no actual mathematical "smoothing"
(ie Gaussian smoothing) is done to the terrain. This can be easily accomplished prior using the FAWE ```//smooth``` command or a smooth brush.

This command also has a pretty good running time of O(n*logk), where n is the number of blocks being changed into stairs/slabs, and k is the average number
of overhangs for each (X,Z) coordinate. Since there are usually under 10 overhangs, it means that the command more or less has an O(n) run time.

# Dim3Smooth Command

This command does a 3-Dimensional Guassian blur on terrain. It is functionally similar to the FAWE //smooth command.
However, the difference is that this command works with caves, overhangs, cliffs, and even large trees. It doesnt matter what the shape of the terrain is, it will smooth it.
The command is ```//dim3smooth <FilterSize> <NumIterations> <Cutoff>```. The first parameter filterSize is the size of the smoothing filter.
This needs to be an odd number greater or equal to 3. The larger the number, the larger the area is sampled for the smoothing. Higher numbers can significantly increase runtime.
The second parameter numIterations is optional, and the default is 1. This parameter sets how many smoothing operations are done.
For example, running "//dim3smooth 3 2" is equivlant to running "//dim3smooth 3" twice.
The third parameter cutoff is also optional and advanced. This parameter sets the cutoff value for when to place blocks or place air. The default is 0.5.
Setting it higher will make objects smaller, and will make caves wider. Setting it lower will make objects larger, but will make caves thinner. Setting it too small will increase
run time, due to how new blocks are decided.

The run time of this command is O(n^3 * (k^3 + s)). n is the width (assuming square) of the selection, k is the width of the filter, and s is the average number of blocks that need
to be searched to add a new one (s is only a consideration if the cutoff is set low).

# TODO

    Add ability to remove specific houses from houseset, and view houses in houseset
    Add permissions support
    Add support for multiple players running commands at once
    Extend city size

# Known Issues

    No permissions currently

# Future Enchancements

    Thermal erosion
    //wall command to automatically generate stone walls for farms
    hexagonal city generator

# Notable References

    Big thanks to Job Talle's blog for the erosion simulation algorithm:
        https://jobtalle.com/simulating_hydraulic_erosion.html

    Most Valuable Stack Overflow Posts:
        https://stackoverflow.com/questions/5281261/generating-a-normal-map-from-a-height-map

    I would be nowhere without the FAWE github:
        https://github.com/IntellectualSites/FastAsyncWorldEdit