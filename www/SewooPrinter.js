var exec = require('cordova/exec');

exports.testPrint = function(successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'SewooPrinter', 'testPrint', []);
};

exports.printPDF = function(base64, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'SewooPrinter', 'printPDF', [base64]);
};

exports.printPDFfromPath = function(filePath, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'SewooPrinter', 'printPDFfromPath', [filePath]);
};

exports.printBitmapFromPDf = function(base64, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'SewooPrinter', 'printBitmapFromPDf', [base64]);
};

exports.printText = function(arrayStrings, successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'SewooPrinter', 'printText', [arrayStrings]);
};

exports.getPrinterStatus = function( successCallback, errorCallback) {
    cordova.exec(successCallback, errorCallback, 'SewooPrinter', 'getPrinterStatus', []);
};