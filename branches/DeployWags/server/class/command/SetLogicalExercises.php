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
            $exercises = "BST Preorder Traversal (Help on)|".
            "BST Inorder Traversal (Help on)|".
            "BST Postorder Traversal (Help on)|".
            "BST Preorder Traversal (Help off)|".
            "BST Inorder Traversal (Help off)|".
            "BST Postorder Traversal (Help off)|".
            "BST Level Traversal 1|".
            "BST Level Traversal 2|";
        }

        if(isset($_POST['InsertNodes']) && $_POST['InsertNodes'] == 'on')
        {
            $exercises = $exercises."Insert Nodes into a BST 1|".
            "Insert Nodes into a BST 2|Insert Nodes into a BST 3|".
            "Insert Nodes into a BST 4|";
        }

        if(isset($_POST['BuildBST']) && $_POST['BuildBST'] == 'on')
        {
            $exercises = $exercises.
            "Binary Search Tree from Postorder Traversal 1|".
            "Binary Search Tree from Postorder Traversal 2|".
            "Binary Search Tree from Postorder Traversal 3|".
            "Binary Search Tree from Postorder Traversal 4|";
        }

        if(isset($_POST['BuildBT']) && $_POST['BuildBT'] == 'on')
        {
            $exercises = $exercises.
            "Binary Tree from Pre/Inorder Traversals 1|".
            "Binary Tree from Pre/Inorder Traversals 2|".
            "Binary Tree from Pre/Inorder Traversals 3|".
            "Binary Tree from Pre/Inorder Traversals 4|";
        }

        if(isset($_POST['RadixSort']) && $_POST['RadixSort'] == 'on')
        {
            $exercises = $exercises."RADIX Sort|".
            "MaxHeap Insertion 1|"."MaxHeap Insertion 2|".
            "MaxHeap Insertion 3|"."MaxHeap Insertion 4|".
            "MaxHeap Deletion 1|"."MaxHeap Deletion 2|".
            "MaxHeap Deletion 3|"."MaxHeap Deletion 4|".
            "MaxHeap 1|"."MaxHeap 2|"."MaxHeap 3|"."MaxHeap 4|".
			"MinHeap Insertion 1|"."MinHeap Insertion 2|".
            "MinHeap Insertion 3|"."MinHeap Insertion 4|".
            "MinHeap Deletion 1|"."MinHeap Deletion 2|".
            "MinHeap Deletion 3|"."MinHeap Deletion 4|".
            "MinHeap 1|"."MinHeap 2|"."MinHeap 3|"."MinHeap 4|".
            "HeapSort Test with Duplicates|"."HeapSort Test without Duplicates|".
            "Testing Heap with Duplicates|";
        }

        $sectionNumber = Auth::getCurrentUser()->getSection();
        $section = Section::getSectionById($sectionNumber);

        $section->setLogicalExercises($exercises);
        $section->save();
        return JSON::success("Logical Microlabs Updated");
    }   
}
