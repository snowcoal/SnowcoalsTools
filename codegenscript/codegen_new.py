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
        'PRISMARINE',
        'SMOOTH_SANDSTONE',
        'RED_SANDSTONE',
        'SMOOTH_RED_SANDSTONE']



with open('output.txt', 'w') as output_file:
    with open('blocks.csv', 'r') as csv_file:
        reader = csv.reader(csv_file)

        t_count = 0
        count = 0
        for row in reader:
            # desired output:
            # blockMap.put(new BlockInput(Blocks.name, xzDirections.xzDir, yDirections.yDir), new BlockOutput(IID, ordinal));

            iid = row[1]
            ordinal = row[2]
            name = 'ERROR'

            for b in blocks:
                if b.lower() in row[0]:
                    name = b

            # slabs
            if(row[3][1] == "E"):
                xzDir = "SLAB"
                btName = "_SLAB"
                subrow = str.split(row[3], '=')
                if(subrow[2] == "bottom"):
                    yDir = "UP"
                else:
                    yDir = "DOWN"
            
            # stairs
            else:
                subrow = str.split(row[3], '=')
                xzDir = subrow[2]
                btName = "_STAIRS"
                if(count < 4):
                    yDir = "UP"
                else:
                    yDir = "DOWN"

            count += 1
            if(count == 10):
                t_count += 1
                count = 0

            output = "blockMap.put(new BlockInput(Blocks."+name+", xzDirections."+xzDir+", yDirections."+yDir+"), new BlockOutput("+iid+", "+ordinal+", BlockTypes."+name+btName+"));\n"

            output_file.write(output)
            