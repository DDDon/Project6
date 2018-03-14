public interface EntityVisitor<R> {


    R visit(Ore ore);
    R visit(OreBlob oreblob);
    R visit(MinerFull minerfull);
    R visit(MinerNotFull minernotfull);
    R visit(Background background);
    R visit(Blacksmith blacksmith);
    R visit(Vein vein);
    R visit(Quake quake);
    R visit(Obstacle obstacle);




}
