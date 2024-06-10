package org.c4marathon.assignment.calculate.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class CalculateId implements Serializable {

    private int calculateId;

    private String receiverId;
}
