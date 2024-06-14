package org.c4marathon.assignment.transfer.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.accounts.entity.Account;
import org.c4marathon.assignment.accounts.repository.AccountRepository;
import org.c4marathon.assignment.transfer.entity.Transfer;
import org.c4marathon.assignment.transfer.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl {

    private final TransferRepository transferRepository;
    @Transactional
    public Account acceptTransfer(Map<String, String> map) throws RuntimeException{
        //userId, transferid 확인
        Long transferId = Long.parseLong(map.get("transferId"));
        String userId = map.get("userId");

        //transfer과 id 일치하는지 확인
        Transfer transfer = transferRepository.findByIdWithLock(transferId)
                .orElseThrow(() -> new RuntimeException("해당 송금 내역 없음"));

        Account receiver = transfer.getReceiver();
        if(isEquals(userId, receiver)) {
            //transfer지우고, 내계좌 증가
            receiver.updateBalance(receiver.getBalance() + transfer.getAmount());
            transferRepository.delete(transfer);
            return receiver;
        }

        return null;
    }
    @Transactional
    public Account cancelTransfer(Map<String, String> map) {
        //userId, transferid 확인
        Long transferId = Long.parseLong(map.get("transferId"));
        String userId = map.get("userId");

        //transfer과 id 일치하는지 확인
        Transfer transfer = transferRepository.findByIdWithLock(transferId)
                .orElseThrow(() -> new RuntimeException("해당 송금 내역 없음"));

        Account sender = transfer.getSender();
        if(isEquals(userId, sender)) {
            //transfer지우고, 내계좌 증가
            sender.updateBalance(sender.getBalance() + transfer.getAmount());
            transferRepository.delete(transfer);
            return sender;
        }


        return null;
    }

    @Transactional
    public void cancelByScheduling(Transfer transfer) throws RuntimeException{

        Account sender = transfer.getSender();
        sender.updateBalance(sender.getBalance() + transfer.getAmount());
        transferRepository.delete(transfer);
    }

    private static boolean isEquals(String userId, Account userAccount) {
        return userAccount.getUser().getUserId().equals(userId);
    }


}
