package com.example.finalproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private CalculationRepository calculationRepository;

    @GetMapping("/calculate")
    public String showCalculator(Model model) {
        model.addAttribute("calculation", new Calculation());
        List<Calculation> calculationHistory = calculationRepository.findAll();
        model.addAttribute("calculationHistory", calculationHistory);
        return "calculator";
    }

    @PostMapping("/calculate")
    public String performCalculation(@ModelAttribute Calculation calculation, Model model,
                                     @RequestParam(name = "operation", defaultValue = "add") String operation) {
        if ("add".equals(operation)) {
            // Addition
            calculation.setSumResult(calculation.getNum1() + calculation.getNum2());
            calculation.setMultiplyResult(0); // Reset multiplication result
            model.addAttribute("message", "Addition result: " + calculation.getSumResult());
        } else if ("multiply".equals(operation)) {
            // Multiplication
            calculation.setMultiplyResult(calculation.getNum1() * calculation.getNum2());
            calculation.setSumResult(0); // Reset addition result
            model.addAttribute("message", "Multiplication result: " + calculation.getMultiplyResult());
        } else if ("divide".equals(operation)) {
            // Division
            if (calculation.getNum2() != 0) {
                calculation.setDivisionResult((double) calculation.getNum1() / calculation.getNum2());
                calculation.setSumResult(0); // Reset addition result
                calculation.setMultiplyResult(0); // Reset multiplication result
                model.addAttribute("message", "Division result: " + calculation.getDivisionResult());
            } else {
                model.addAttribute("message", "Cannot divide by zero!");
            }
        } else if ("subtract".equals(operation)) {
            // Subtraction
            calculation.setSubtractionResult(calculation.getNum1() - calculation.getNum2());
            calculation.setSumResult(0); // Reset addition result
            calculation.setMultiplyResult(0); // Reset multiplication result
            calculation.setDivisionResult(0); // Reset division result
            model.addAttribute("message", "Subtraction result: " + calculation.getSubtractionResult());
        } else if ("modulus".equals(operation)) {
            // Modulus
            if (calculation.getNum2() != 0) {
                calculation.setModulusResult(calculation.getNum1() % calculation.getNum2());
                calculation.setSumResult(0); // Reset addition result
                calculation.setMultiplyResult(0); // Reset multiplication result
                calculation.setDivisionResult(0); // Reset division result
                calculation.setSubtractionResult(0); // Reset subtraction result
                model.addAttribute("message", "Modulus result: " + calculation.getModulusResult());
            } else {
                model.addAttribute("message", "Cannot calculate modulus with zero!");
            }
        }

        calculationRepository.save(calculation);
        List<Calculation> calculationHistory = calculationRepository.findAll();
        model.addAttribute("calculationHistory", calculationHistory);
        return "calculator";
    }

    @GetMapping("/history")
    public String showCalculationHistory(Model model) {
        List<Calculation> calculationHistory = calculationRepository.findAll();
        model.addAttribute("calculationHistory", calculationHistory);
        return "history";
    }
}
