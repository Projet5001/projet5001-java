package com.projet5001.game.ai;

import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class Astar {

    private static final int COUT = 32/16;

    public  static ArrayList<Node> run(Node nodeStart, Node dest){
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closeList = new ArrayList<>();
        ArrayList<Node> path  = new ArrayList<>();

        Node current;

        openList.add(nodeStart);

        nodeStart.setG(0);
        nodeStart.setH(calculHeuristique(nodeStart, dest));
        nodeStart.setF(nodeStart.getG() + calculHeuristique(nodeStart, dest));

        while (!openList.isEmpty()){
            current = openList.get(0);

            if (calculHeuristique(current,dest) < 1){
                openList.clear();
                closeList.clear();
                return reconstruct_path(path, current);
            }

            openList.remove(0);
            closeList.add(current);

            for(Node neighbour: current.getneighbours()){

                if (neighbour.getG() == 0){
                    neighbour.setG(current.getG() + movementCost(current, neighbour));
                }

                if (lisContains(closeList,neighbour)){
                    continue;
                }

                int tentativeGCost = current.getG() + movementCost(current, neighbour);

                if (!lisContains(openList,neighbour) || tentativeGCost < neighbour.getG()){
                    neighbour.setParent(current);
                    neighbour.setG(tentativeGCost);
                    neighbour.setF(neighbour.getG() + calculHeuristique(neighbour,dest));
                    if (!lisContains(openList,neighbour)){
                        openList.add(neighbour);
                        Collections.sort(openList,new FValueComarator());
                    }
                }
            }
        }
        return null;
    }
    private static ArrayList<Node> reconstruct_path(ArrayList<Node> path, Node current){
        if (current.getParent() != null ){
            path = reconstruct_path(path, current.getParent());
            path.add(current);
        }
        return path;
    }

    private static int movementCost(Node current, Node neighbour){
        return current.getSpeed(); //-neighbour malus cost
    }


    private static Boolean lisContains(ArrayList<Node> list, Node node){
        for (Node n: list){
          if(n.equals(node)){
              return true;
          }
        }
        return false;
    }

    private static double  calculHeuristique(Node current, Node dest) {
        Vector2 vector2Current = current.getKeyFromVector(current);
        Vector2 vector2Dest = current.getKeyFromVector(dest);
        return Math.sqrt(Math.pow(vector2Current.x - vector2Dest.x, 2) + (Math.pow(vector2Current.y - vector2Dest.y, 2))) * COUT;
    }

    private static class FValueComarator implements Comparator<Node>{

        @Override
        public int compare(Node node, Node node2) {
            return node.getF() < node2.getF() ? -1 : node.getF() == node2.getF() ? 0 : 1;
        }
    }
}
