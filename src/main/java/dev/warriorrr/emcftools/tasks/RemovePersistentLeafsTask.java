package dev.warriorrr.emcftools.tasks;

//import org.bukkit.Bukkit;
//import org.bukkit.Chunk;
//import org.bukkit.Tag;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockState;
//import org.bukkit.block.data.type.Leaves;

public class RemovePersistentLeafsTask implements Runnable {
    @Override
    public void run() {
        //Might use this later someday, needs optimization
        return;
        /*
        Chunk[] loadedChunks = Bukkit.getWorlds().get(0).getLoadedChunks();
        System.out.println("Starting persistent leaf removal task, loaded chunks: " + loadedChunks.length);
        int skippedChunks = 0;
        long startTime = System.nanoTime();
        for (Chunk chunk : loadedChunks) {
            if (checkedChunks.contains(chunk.getX() + ", " + chunk.getZ())) {
                skippedChunks++;
                continue;
            }
            for (int x = 0; x < 15; x++) {
                for (int y = 60; y < chunk.getWorld().getMaxHeight(); y++) {
                    for (int z = 0; z < 15; z++) {
                        Block block = chunk.getBlock(x, y, z);
                        if (Tag.LEAVES.isTagged(block.getType())) {
                            BlockState blockState = block.getState();
                            Leaves leafBlock = ((Leaves) blockState.getBlockData());
                            if (leafBlock.isPersistent()) {
                                leafBlock.setPersistent(false);
                                blockState.setBlockData(leafBlock);
                                block.setBlockData(blockState.getBlockData());
                            }
                        }
                    }
                }
            }
            checkedChunks.add(chunk.getX() + ", " + chunk.getZ());
        }

        for (int i = 0; i < checkedChunks.size(); i++) {
            int chunkX = Integer.parseInt(checkedChunks.get(i).split(", ")[0]);
            int chunkZ = Integer.parseInt(checkedChunks.get(i).split(", ")[1]);
            System.out.println(chunkX);
            System.out.println(chunkZ);
        }

        long totalTime = System.nanoTime() - startTime;
        System.out.println("Persistent leaf removal task done in " + totalTime / 1000000 + " ms.");
        System.out.println("Chunks skipped: " + skippedChunks);
        */
    }
}
