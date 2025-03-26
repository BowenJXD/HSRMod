// Function to resize and center an image on a new canvas
function resizeAndCenterImageOnCanvas(inputFile, outputFolder, canvasWidth, canvasHeight, imageWidth, imageHeight) {
    var doc = open(inputFile); // Open the image

    // Resize the image to the specified image size (imageWidth x imageHeight)
    doc.resizeImage(UnitValue(imageWidth, "px"), UnitValue(imageHeight, "px"));

    // Resize the canvas to the specified canvas size (canvasWidth x canvasHeight)
    doc.resizeCanvas(UnitValue(canvasWidth, "px"), UnitValue(canvasHeight, "px"), AnchorPosition.MIDDLECENTER);

    var name = inputFile.name.replace("_p", ""); // Remove "_p" from the file name
    // Create a save file object with a .png extension
    var saveFile = new File(outputFolder + "/" + name.replace(/\.[^\.]+$/, ".png"));

    // Set save options (PNG in this case)
    var pngOptions = new PNGSaveOptions();

    // Save the resized image
    doc.saveAs(saveFile, pngOptions, true);

    // Close the document
    doc.close(SaveOptions.DONOTSAVECHANGES);
}

// Function to process all images in a folder
function processFolder(inputFolderPath, canvasWidth, canvasHeight, imageWidth, imageHeight) {
    var inputFolder = new Folder(inputFolderPath);
    var outputFolder = new Folder(inputFolderPath + "/OUTPUT");

    // Check if output folder exists, if not, create it
    if (!outputFolder.exists) {
        outputFolder.create();
    }

    // Get all files in the input folder
    var files = inputFolder.getFiles(/\.(jpg|jpeg|png|tif|tiff)$/i); // Supports jpg, jpeg, png, tif, tiff

    // Process each file
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        if (file instanceof File) {
            resizeAndCenterImageOnCanvas(file, outputFolder.fsName, canvasWidth, canvasHeight, imageWidth, imageHeight);
        }
    }
}

// Main function
function main() {
    // Ask user to select the input folder
    var inputFolder = Folder.selectDialog("Select the folder with images to resize:");

    if (inputFolder) {
        // Set canvas and image size (change these values as needed)
        var canvasWidth = 512;   // Set canvas width
        var canvasHeight = 512;  // Set canvas height
        var imageWidth = 512;     // Set image width
        var imageHeight = 512;    // Set image height

        // Process the folder and save to OUTPUT subfolder
        processFolder(inputFolder.fsName, canvasWidth, canvasHeight, imageWidth, imageHeight);
        alert("Batch resizing and centering on canvas complete!");
    } else {
        alert("No folder selected. Script cancelled.");
    }
}

// Run the main function
main();
