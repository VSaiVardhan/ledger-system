package com.vardhan.ledger.controller;

import com.vardhan.ledger.dto.TransactionRequest;
import com.vardhan.ledger.model.Transaction;
import com.vardhan.ledger.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transactions", description = "Operations for creating and managing transactions")
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public String addTransaction(@Valid @RequestBody Transaction transaction) {
        return transactionService.addTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PutMapping("/{id}")
    public Transaction updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

    @DeleteMapping("/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id);
    }

    // @Operation(
    //     summary = "Create double-entry transaction",
    //     description = "Creates a transaction ensuring total debit equals total credit"
    // )
    // @PostMapping("/double")
    // public String createDoubleEntry(@RequestBody TransactionRequest request) {
    //     return transactionService.createDoubleEntryTransaction(request);
    // }

    @Operation(summary = "Create double-entry transaction")
    @PostMapping("/double")
    public String createDoubleEntry(
        @RequestBody(
            description = "Double-entry transaction request",
            required = true,
            content = @io.swagger.v3.oas.annotations.media.Content(
                examples = @ExampleObject(
                    value = """
                    {
                    "description": "Salary received",
                    "date": "2026-04-04",
                    "entries": [
                        {
                        "accountId": 1,
                        "type": "DEBIT",
                        "amount": 50000
                        },
                        {
                        "accountId": 2,
                        "type": "CREDIT",
                        "amount": 50000
                        }
                    ]
                    }
                    """
                )
            )
        )
        TransactionRequest request
    ) {
        return transactionService.createDoubleEntryTransaction(request);
    }

    @Operation(
        summary = "Reverse a transaction",
        description = "Creates a reversal transaction with opposite entries instead of deleting"
    )
    @PostMapping("/{id}/reverse")
    public String reverseTransaction(@PathVariable Long id) {
        return transactionService.reverseTransaction(id);
    }

}