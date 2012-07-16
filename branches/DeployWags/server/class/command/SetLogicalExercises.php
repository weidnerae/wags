<?php

/**
*SetLogicalExercises.php
*
*Author: Philip Meznar
*
*This class updates the logicalExercises column
*of an entry in the section table.
*
*
*This is so gross.  There's gotta be a better way..
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

        if(isset($_POST['MaxHeapInsert']) && $_POST['MaxHeapInsert'] == 'on')
        {
            $exercises = $exercises.
            "MaxHeap Insertion 1|"."MaxHeap Insertion 2|".
            "MaxHeap Insertion 3|"."MaxHeap Insertion 4|";
        }

        if(isset($_POST['MaxHeapDelete']) && $_POST['MaxHeapDelete'] == 'on')
        {
            $exercises = $exercises.
            "MaxHeap Deletion 1|"."MaxHeap Deletion 2|".
            "MaxHeap Deletion 3|"."MaxHeap Deletion 4|";
        }

        if(isset($_POST['MinHeapInsert']) && $_POST['MinHeapInsert'] == 'on')
        {
            $exercises = $exercises.
			"MinHeap Insertion 1|"."MinHeap Insertion 2|".
            "MinHeap Insertion 3|"."MinHeap Insertion 4|";
        }

        if(isset($_POST['MinHeapDelete']) && $_POST['MinHeapDelete'] == 'on')
        {
            $exercises = $exercises.
            "MinHeap Deletion 1|"."MinHeap Deletion 2|".
            "MinHeap Deletion 3|"."MinHeap Deletion 4|";
        }

        if(isset($_POST['MaxHeapBuild']) && $_POST['MaxHeapBuild'] == 'on')
        {
            $exercises = $exercises.
            "MaxHeap 1|"."MaxHeap 2|"."MaxHeap 3|"."MaxHeap 4|";
        }

        if(isset($_POST['MinHeapBuild']) && $_POST['MinHeapBuild'] == 'on')
        {
            $exercises = $exercises.
            "MinHeap 1|"."MinHeap 2|"."MinHeap 3|"."MinHeap 4|";
        }
        
        if(isset($_POST['HeapSort']) && $_POST['HeapSort'] == 'on')
        {
            $exercises = $exercises.
            "HeapSort 1|"."HeapSort 2|"."HeapSort 3|"."HeapSort 4|";
        }

        if(isset($_POST['RadixSort']) && $_POST['RadixSort'] == 'on')
        {
            $exercises = $exercises."RADIX Sort|Radix Sort 2|";

        }

        if(isset($_POST['Kruskal']) && $_POST['Kruskal'] == 'on')
        {
            $exercises = $exercises."MST Kruskal's 1|".
            "MST Kruskal's 2|MST Kruskal's 3|".
            "MST Kruskal's 4|"; 
        }

        if(isset($_POST['Prim']) && $_POST['Prim'] == 'on')
        {
            $exercises = $exercises."MST Prim's 1|".
            "MST Prim's 2|MST Prim's 3|".
            "MST Prim's 4|"; 
        }

        $sectionNumber = Auth::getCurrentUser()->getSection();
        $section = Section::getSectionById($sectionNumber);

        $section->setLogicalExercises($exercises);
        $section->save();
        return JSON::success("Logical Microlabs Updated");
    }   
}
