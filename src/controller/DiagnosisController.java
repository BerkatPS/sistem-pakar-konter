package controller;

// interfaces/DiagnosisController.java
import usecases.*;
import entities.*;

public class DiagnosisController {
    private DiagnoseUseCase diagnoseUseCase;

    public DiagnosisController(DiagnoseUseCase diagnoseUseCase) {
        this.diagnoseUseCase = diagnoseUseCase;
    }

    public Diagnosis handleDiagnosisRequest(String keluhan) {
        return diagnoseUseCase.diagnose(keluhan);
    }
}
