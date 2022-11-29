Plugin for Intellij IDEA for aligning comments in java code

Use case:

Select code sample with comments that you want to align

```java
class Solution() {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR); // some 
    var project = e.getRequiredData(CommonDataKeys.PROJECT); // nasty
    var document = editor.getDocument(); // unaligned
    var model = editor.getSelectionModel(); // comments
}
```

press `ctrl+alt+c` and choose `Align comments` option

```java
class Solution() {
    var editor = e.getRequiredData(CommonDataKeys.EDITOR);   // some 
    var project = e.getRequiredData(CommonDataKeys.PROJECT); // nasty
    var document = editor.getDocument();                     // unaligned
    var model = editor.getSelectionModel();                  // comments
}
```

vua-la, your comments are aligned 

Author
: E.Saratovtsev M33001