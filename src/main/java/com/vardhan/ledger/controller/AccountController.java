package com.vardhan.ledger.controller;

import com.vardhan.ledger.dto.StatementResponse;
import com.vardhan.ledger.dto.SummaryResponse;
import com.vardhan.ledger.model.Account;
import com.vardhan.ledger.service.AccountService;
import org.springframework.web.bind.annotation.*;
// import com.vardhan.ledger.model.Entry;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Accounts", description = "Operations related to account management and statements")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @Operation(
        summary = "Get account statement",
        description = "Fetch all transactions for an account with optional date filtering"
    )
    @GetMapping("/{id}/statement")
    public List<StatementResponse> getStatement(
            @PathVariable Long id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return accountService.getAccountStatement(id, from, to);
    }

    @Operation(
        summary = "Get monthly summary",
        description = "Returns total debit, credit, and net balance change for a given month"
    )
    @GetMapping("/{id}/summary")
    public SummaryResponse getSummary(
            @PathVariable Long id,
            @RequestParam String month,
            @RequestParam String year
    ) {
        return accountService.getMonthlySummary(id, month, year);
    }

    
}