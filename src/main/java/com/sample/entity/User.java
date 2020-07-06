package com.sample.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * UserのEntityクラス.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * ユーザー名の最大桁数.
     */
    private static final int NAME_MAX_LENGTH = 255;
    /**
     * Eメールアドレスの最大桁数.
     */
    private static final int EMAIL_ADDRESS_MAX_LENGTH = 255;
    /**
     * 携帯電話番号の桁数.
     */
    private static final int CELL_PHONE_NUMBER_LENGTH = 11;

    /**
     * ユーザーID.
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;

    /**
     * ユーザー名.
     */
    @Column(name = "name", nullable = false)
    @NotBlank
    @Length(max = NAME_MAX_LENGTH)
    private String name;

    /**
     * Eメールアドレス.
     */
    @Column(name = "email_address", nullable = false)
    @NotBlank
    @Length(max = EMAIL_ADDRESS_MAX_LENGTH)
    @Email
    private String emailAddress;

    /**
     * 携帯電話番号.
     */
    @Column(name = "cell_phone_number")
    @Length(min = CELL_PHONE_NUMBER_LENGTH, max = CELL_PHONE_NUMBER_LENGTH)
    @Pattern(regexp = "[0-9]*")
    private String cellPhoneNumber;
}
