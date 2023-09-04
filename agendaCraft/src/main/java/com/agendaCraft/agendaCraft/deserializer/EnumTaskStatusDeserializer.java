package com.agendaCraft.agendaCraft.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.agendaCraft.agendaCraft.enums.EnumTaskStatus;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class EnumTaskStatusDeserializer extends JsonDeserializer<EnumTaskStatus> {
    @Override
    public EnumTaskStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String statusValue = jsonParser.getValueAsString();
        if (statusValue != null) {
            // Map status strings to enum values here
            if (statusValue.equalsIgnoreCase("In Progress")) {
                return EnumTaskStatus.IN_PROGRESS;
            } else if (statusValue.equalsIgnoreCase("Pending")) {
                return EnumTaskStatus.PENDING;
            } else if (statusValue.equalsIgnoreCase("Completed")) {
                return EnumTaskStatus.COMPLETED;
            } else if (statusValue.equalsIgnoreCase("Canceled")) {
                return EnumTaskStatus.CANCELED;
            } else if (statusValue.equalsIgnoreCase("Expired")) {
                return EnumTaskStatus.EXPIRED;
            }
        }
        return null; // Handle invalid or unknown status values
    }
}