ALTER TABLE client MODIFY rg_state_registration varchar(100) NULL;
ALTER TABLE client MODIFY date_birth_company_formation datetime NULL;
DROP INDEX rg_state_registration ON client;
