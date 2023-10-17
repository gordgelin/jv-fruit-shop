package core.basesyntax.service.impl;

import core.basesyntax.db.dto.StorageItemDTO;
import core.basesyntax.db.dto.StorageOperationDTO;
import core.basesyntax.service.ParserService;
import java.util.ArrayList;
import java.util.List;

public class CsvToStorageOperationDtoParserService implements ParserService<StorageOperationDTO> {
    private static final int OPERATION_TYPE_FILED_INDEX = 0;
    private static final int STORAGE_ITEM_NAME_FILED_INDEX = 1;
    private static final int STORAGE_ITEM_QUANTITY_FIELD_INDEX = 2;
    private static final int CSV_MIN_FIELDS_NUMBER = 3;
    private static final String CSV_COMMA_SEPARATOR = ",";
    private final List<String> data;
    private final boolean headersPresent;

    public CsvToStorageOperationDtoParserService(List<String> data, boolean headersPresent) {
        this.data = data;
        this.headersPresent = headersPresent;
    }

    @Override
    public List<StorageOperationDTO> parse() {
        List<StorageOperationDTO> storageOperationList = new ArrayList<>();
        int startLine = headersPresent ? 0 : 1;

        for (int i = startLine; i < data.size(); i++) {
            String[] csvDataLine = data.get(i).split(CSV_COMMA_SEPARATOR);

            try {
                if (csvDataLine.length < CSV_MIN_FIELDS_NUMBER
                        || csvDataLine[0].isBlank()
                        || csvDataLine[1].isBlank()
                        || csvDataLine[2].isBlank()) {
                    throw new RuntimeException("Parsing failed! Invalid data at line: " + i);
                }

                StorageItemDTO storageItem =
                        new StorageItemDTO(csvDataLine[STORAGE_ITEM_NAME_FILED_INDEX],
                        Double.parseDouble(csvDataLine[STORAGE_ITEM_QUANTITY_FIELD_INDEX].trim()));

                StorageOperationDTO storageOperation =
                        new StorageOperationDTO(csvDataLine[OPERATION_TYPE_FILED_INDEX],
                        storageItem);

                storageOperationList
                        .add(storageOperation);

            } catch (NumberFormatException e) {
                throw new RuntimeException("Parsing failed! Invalid data at line: " + i, e);
            }
        }

        return storageOperationList;
    }
}
