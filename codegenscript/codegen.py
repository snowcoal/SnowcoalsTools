import csv

blocks = [
        'STONE',
        'ANDESITE',
        'DIORITE',
        'GRANITE',
        'COBBLESTONE',
        'MOSSY_COBBLESTONE',
        'BLACKSTONE',
        'COBBLED_DEEPSLATE',
        'SANDSTONE',
        'SMOOTH_SANDSTONE',
        'RED_SANDSTONE',
        'SMOOTH_RED_SANDSTONE']


with open('output.txt', 'w') as output_file:
    with open('blocks.csv', 'r') as csv_file:
        reader = csv.reader(csv_file)

        for row in reader:
            # desired output:
            # blockMap.put(new BlockInput(Blocks.name, xzDirections.xzDir, yDirections.yDir), new BlockOutput(IID, ordinal));

            # minecraft:stone_stairs,270733,10671,{DirectionalProperty{name=facing}=NORTH, EnumProperty{name=half}=bottom, EnumProperty{name=shape}=straight, BooleanProperty{name=waterlogged}=false}
            # minecraft:stone_slab,5533,11301,{EnumProperty{name=type}=bottom, BooleanProperty{name=waterlogged}=false}

            iid = row[1]
            ordinal = row[2]
            name = str.split(str.split(row[0], ':')[1], '_')[0].upper()
            type = "_" + str.split(str.split(row[0], ':')[1].upper(), name + "_")[1].upper()

            fullName = name + type
            if "SLAB" in fullName:
                fullName = fullName.replace("_SLAB", "")
            if "STAIRS" in fullName:
                fullName = fullName.replace("_STAIRS", "")

            # slabs
            if ("SLAB" in type):
                xzDir = "SLAB"
                topBottom = str.split(row[3], "EnumProperty{name=type}=")[1]
                if(topBottom == "top"):
                    yDir = "DOWN"
                else:
                    yDir = "UP"
            
            # stairs
            else:
                # print(type)
                xzDir = str.split(row[3], "DirectionalProperty{name=facing}=")[1]
                topBottom = str.split(row[4], "EnumProperty{name=half}=")[1]
                if(topBottom == "top"):
                    yDir = "DOWN"
                else:
                    yDir = "UP"

            print(name+type + ", " + iid + ", " + xzDir + ", " + yDir)

            output = "blockMap.put(new BlockInput(Blocks." + fullName + ", xzDirections." + xzDir + ", yDirections." + yDir + "), new BlockOutput(" + iid + ", " + ordinal + ", BlockTypes." + fullName + "));\n"

            output_file.write(output)
            