<?php

/**
*SetLogicalExercises.php
*
*Author: Philip Meznar
*
*This class updates the logicalExercises column
*of an entry in the section table.
*/

class SetLogicalExercises extends Command
{
    public function execute(){

        $exercises = "";
        
        if(isset($_POST['Traversals']) && $_POST['Traversals'] == 'on')
        {
            $exercises = $exercises."BST Preorder Traversal (Help on)|BST Inorder Traversal (Help on)|BST Postorder Traversal (Help on)|BST Preorder Traversal (Help off)|BST Inorder Traversal (Help off)|BST Postorder Traversal (Help off)|";
        }

        if(isset($_POST['InsertNodes']) && $_POST['InsertNodes'] == 'on')
        {
            $exercises = $exercises."Insert Nodes into a BST 1|Insert Nodes into a BST 2|Insert Nodes into a BST 3|Insert Nodes into a BST 4|";
        }

        if(isset($_POST['BuildBST']) && $_POST['BuildBST'] == 'on')
        {
            $exercises = $exercises."Binary Search Tree from Postorder Traversal 1|Binary Search Tree from Postorder Traversal 2|Binary Search Tree from Postorder Traversal 3|Binary Search Tree from Postorder Traversal 4|";
        }

        if(isset($_POST['BuildBT']) && $_POST['BuildBT'] == 'on')
        {
            $exercises = $exercises."Binary Tree from Pre/Inorder Traversals 1|Binary Tree from Pre/Inorder Traversals 2|Binary Tree from Pre/Inorder Traversals 3|Binary Tree from Pre/Inorder Traversals 4|";
        }

        $sectionNumber = Auth::getCurrentUser()->getSection();
        $section = Section::getSectionById($sectionNumber);

        $section->setLogicalExercises($exercises);
        return JSON::success("Logical Microlabs Updated");
    }   
}
