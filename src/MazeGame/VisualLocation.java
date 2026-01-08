package MazeGame;

import java.util.EnumSet;

public class VisualLocation {

    private EnumSet<Direction> exits;

    public VisualLocation(boolean north, boolean east, boolean south, boolean west) {
        exits = EnumSet.noneOf(Direction.class);
        if (north) exits.add(Direction.NORTH);
        if (east)  exits.add(Direction.EAST);
        if (south) exits.add(Direction.SOUTH);
        if (west)  exits.add(Direction.WEST);
    }

    public boolean hasNorth() { return exits.contains(Direction.NORTH); }
    public boolean hasEast()  { return exits.contains(Direction.EAST); }
    public boolean hasSouth() { return exits.contains(Direction.SOUTH); }
    public boolean hasWest()  { return exits.contains(Direction.WEST); }

    public int exitCount() {
        return exits.size();
    }

    public String getImageName() {
        if (exits.size() == 4) return "nesw.jpg";

        if (exits.size() == 3) {
            if (!hasSouth()) return "wne.jpg";
            if (!hasWest())  return "nes.jpg";
            if (!hasNorth()) return "esw.jpg";
            if (!hasEast())  return "swn.jpg";
        }

        if (exits.size() == 2) {
            if (hasNorth() && hasSouth()) return "ns.jpg";
            if (hasEast() && hasWest())   return "we.jpg";
            if (hasNorth() && hasEast())  return "ne.jpg";
            if (hasEast() && hasSouth())  return "es.jpg";
            if (hasSouth() && hasWest())  return "sw.jpg";
            if (hasWest() && hasNorth())  return "wn.jpg";
        }

        if (exits.size() == 1) {
            if (hasNorth()) return "ts.jpg";
            if (hasEast())  return "tw.jpg";
            if (hasSouth()) return "tn.jpg";
            if (hasWest())  return "te.jpg";
        }

        return "unknown.jpg";
    }
}
