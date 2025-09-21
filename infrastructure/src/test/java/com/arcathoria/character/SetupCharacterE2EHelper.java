package com.arcathoria.character;

import com.arcathoria.UUIDGenerator;
import com.arcathoria.account.AccountManagerE2EHelper;
import com.arcathoria.character.dto.CharacterDTO;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public class SetupCharacterE2EHelper {

    private final AccountManagerE2EHelper accountManagerE2EHelper;
    private final CreateCharacterE2EHelper createCharacterE2EHelper;
    private final SelectCharacterE2EHelper selectCharacterE2EHelper;

    public SetupCharacterE2EHelper(final TestRestTemplate restTemplate) {
        this.accountManagerE2EHelper = new AccountManagerE2EHelper(restTemplate);
        this.createCharacterE2EHelper = new CreateCharacterE2EHelper(restTemplate);
        this.selectCharacterE2EHelper = new SelectCharacterE2EHelper(restTemplate);
    }

    public CharacterWithAccountContext setupSelectedCharacterWithAccount() {
        HttpHeaders account = accountManagerE2EHelper.registerAndGetAuthHeaders("acc_" + UUIDGenerator.generate(15) + "@arcathoria.arcathoria");
        ResponseEntity<CharacterDTO> character = createCharacterE2EHelper.create(CreateCharacterDTOMother.aCreateCharacterDTO().build(), account);
        CharacterDTO selectCharacter = selectCharacterE2EHelper.selectCharacter(character.getBody().id(), account);

        return new CharacterWithAccountContext(account, selectCharacter);
    }
}
