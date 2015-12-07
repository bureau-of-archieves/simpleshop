package simpleshop.common;


public enum ObjectGraphInspectionResult {
    CONTINUE, //check the properties of the current target object
    BYPASS, //ignore the properties of the current target object
    ABORT //error occurred while processing the current target object
}