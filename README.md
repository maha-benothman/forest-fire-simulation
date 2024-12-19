# Forest Fire Simulation

## Objective
The goal of this project is to implement a simulation of forest fire propagation.

## Description
The forest is represented as a grid of dimensions `h x l`. The simulation progresses in discrete time steps. Initially, one or more cells in the grid are on fire. 

At each time step:
- A cell that is on fire burns out and becomes ash (it cannot burn again).
- There is a probability `p` that the fire spreads to each of the 4 adjacent cells.

The simulation ends when no cell is on fire.
