def maze_solver(maze):
    trav_dir = 1; #traveling direction of light (-1->left, 0->up, 1->right, 2->down)
    cur_pos = [0, 0] #current position of the light ray
    num_rows, num_cols = len(maze), len(maze[0])
    lr_trajectory = [] #light ray trajectory
    lr_trajectory.append((cur_pos[0], cur_pos[1])) #append the initial position of the light ray when it enters the maze
    while cur_pos[0] < num_rows and cur_pos[0] >= 0 and cur_pos[1] < num_cols and cur_pos[1] >= 0:
        if(trav_dir == 1): #moving from left to right
            while(cur_pos[1] < num_cols and maze[cur_pos[0]][cur_pos[1]] == 0):
                cur_pos[1] = cur_pos[1] + 1 #increment column index because the light ray is moving from left to right
                lr_trajectory.append((cur_pos[0], cur_pos[1]))
            if cur_pos[1] < num_cols:
                if (maze[cur_pos[0]][cur_pos[1]] == 1):
                    trav_dir = 0; #light ray will now travel upward
                    cur_pos[0] = cur_pos[0] - 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))
                elif (maze[cur_pos[0]][cur_pos[1]] == -1):
                    trav_dir = 2; #light ray will now travel downward
                    cur_pos[0] = cur_pos[0] + 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))

        elif(trav_dir == 0): #moving upward
            while(cur_pos[0] >= 0 and maze[cur_pos[0]][cur_pos[1]] == 0):
                cur_pos[0] = cur_pos[0] - 1 #decrement the row index because the light ray is moving upward
                lr_trajectory.append((cur_pos[0], cur_pos[1]))
            if cur_pos[0] >= 0:
                if (maze[cur_pos[0]][cur_pos[1]] == 1):
                    trav_dir = 1; #light ray will now travel from left to right
                    cur_pos[1] = cur_pos[1] + 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))
                elif (maze[cur_pos[0]][cur_pos[1]] == -1):
                    trav_dir = -1; #light ray will now travel from right to left
                    cur_pos[1] = cur_pos[1] - 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))

        elif(trav_dir == -1): #moving from right to left
            while(cur_pos[1] >= 0 and maze[cur_pos[0]][cur_pos[1]] == 0):
                cur_pos[1] = cur_pos[1] - 1 #decrement the column index because the light ray is moving from right to left
                lr_trajectory.append((cur_pos[0], cur_pos[1]))
            if cur_pos[1] >= 0:
                if (maze[cur_pos[0]][cur_pos[1]] == 1):
                    trav_dir = 2; #light ray will now travel downward
                    cur_pos[0] = cur_pos[0] + 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))
                elif (maze[cur_pos[0]][cur_pos[1]] == -1):
                    trav_dir = 0; #light ray will now travel upward
                    cur_pos[0] = cur_pos[0] - 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))

        else: #moving downward
            while(cur_pos[0] < num_rows and maze[cur_pos[0]][cur_pos[1]] == 0):
                cur_pos[0] = cur_pos[0] + 1 #increment the row index because the light ray is moving downward   
                lr_trajectory.append((cur_pos[0], cur_pos[1]))
            
            if (cur_pos[0] < num_rows):

                if (maze[cur_pos[0]][cur_pos[1]] == 1):
                    trav_dir = -1; #light ray will now travel from right to left
                    cur_pos[1] = cur_pos[1] - 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))
                elif (maze[cur_pos[0]][cur_pos[1]] == -1):
                    trav_dir = 1; #light ray will now travel from left to right
                    cur_pos[1] = cur_pos[1] + 1 #move the light ray to its new position
                    lr_trajectory.append((cur_pos[0], cur_pos[1]))

    return lr_trajectory[0:len(lr_trajectory) - 1]

def main():
    maze = [[0, 0, 0, -1],
            [1, 0, 0, 1], 
            [0, 0, 0, 0], 
            [-1, 0, 0, 0]]
    lr_trajectory = maze_solver(maze)
    print(lr_trajectory)



if __name__ == "__main__":
    main()
