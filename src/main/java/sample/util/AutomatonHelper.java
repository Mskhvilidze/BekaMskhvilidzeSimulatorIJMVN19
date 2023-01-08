package sample.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;

public class AutomatonHelper {
    private File file;

    public AutomatonHelper(File file) {
        this.file = file;
    }

    public void createAutomaton(String name) throws IOException {
        String text;
        if (name.equals("GameOfLifeAutomaton.txt")) {
            text = getGameOfLife().replace("\n", System.lineSeparator());
        } else {
            text = getKruemelmonster().replace("\n", System.lineSeparator());
        }
        ArrayList<String> lines = new ArrayList<>();
        lines.add(text);
        if (!Files.exists(file.toPath())) {
            Files.createFile(file.toPath());
            Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        }
    }

    private String getGameOfLife() {
        return "package automata;\n" + "import sample.model.AbstractAutomaton;\n" + "import sample.model.Callable;\n" +
               "public class Automata extends AbstractAutomaton {\n" + "private final static int rows = 30;\n" +
               "private final static int columns = 50;\n" + "private final static int numberOfStates = 2;\n" +
               "private final static boolean isTorus = false;\n" + "\n" + "    public Automata() {\n" +
               "        super(rows, columns, numberOfStates, isTorus);\n" + "    }\n" + "\n" + "    @Override\n" +
               "    public Cell transform(Cell cell, Cell[] neighbors) {\n" + "        int numberOfNeighbors = 0;\n" +
               "        for (Cell neighbor : neighbors) {\n" + "            if (neighbor.getState() == 1) {\n" +
               "                numberOfNeighbors++;\n" + "            }\n" + "        }\n" + "        if (cell.getState() == 0) {\n" +
               "            if (numberOfNeighbors == 3) {\n" + "                return new Cell(1);\n" + "            } else {\n" +
               "                return new Cell(cell);\n" + "            }\n" + "        } else {\n" +
               "            if (numberOfNeighbors < 2 || numberOfNeighbors > 3) {\n" + "                return new Cell(0);\n" +
               "            } else {\n" + "                return new Cell(cell);\n" + "            }\n" + "        }\n" + "    }\n" +
               "\n" + "    @Callable\n" + "    public void setGlider(int i, int j) {\n" + "        setState(i, j, 1);\n" +
               "        setState(i + 1, j, 1);\n" + "        setState(i + 2, j, 1);\n" + "    }\n" + "\n" + "   @Callable\n" +
               "    public void setOscillator(int i, int j) {\n" + "     setState(i, j,1);\n" + "     setState(i + 1, j + 1,1);\n" +
               "     setState(i + 2, j - 1,1);\n" + "     setState(i + 2, j,1);\n" + "     setState(i + 2, j + 1,1);\n" + "     }\n" +
               "     \n" + "     @Callable\n" + "      public void setCross(int i, int j){\n" + "       setState(i, j,1);\n" +
               "       setState(i, j - 1, 1);\n" + "       setState(i, j + 1, 1);\n" + "       setState(i - 1, j, 1);\n" +
               "       setState(i + 1, j, 1);\n" + "      }\n" + "}\n";
    }

    private String getKruemelmonster() {
        return "package automata;\n" + "import sample.model.AbstractAutomaton;\n" + "\n" +
               "  public class Automata extends AbstractAutomaton{\n" + "\n" + "  private final static int rows = 30;\n" +
               "  private final static int columns = 50;\n" + "  private final static int numberOfStates = 2;\n" +
               "  private final static boolean isTorus = false;\n" + " \n" + "  public Automata() {\n" +
               "        super(rows, columns, numberOfStates, isTorus);\n" + "    }\n" + "\n" +
               "    public Cell transform(Cell cell, Cell[] neighbors) {\n" + "        int state;\n" +
               "        for (Cell neighbor : neighbors) {\n" +
               "            state = (cell.getState() + 1) < getNumberOfStates() ? cell.getState() + 1 : 0;\n" +
               "            if (state == neighbor.getState()) {\n" + "                return new Cell(neighbor.getState());\n" +
               "            }\n" + "        }\n" + "        return new Cell(cell);\n" + "    }\n" + "    }";
    }
}
