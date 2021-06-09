package ru.diasoft.digitalq.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name="dq_smsverify")
public class SmsVerification implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
    @SequenceGenerator(name="dq_smsverify_seq", sequenceName = "dq_smsverify_seq", allocationSize = 2)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dq_smsverify_seq")
    @Column(name="smsVerifyId")
	protected Long smsVerifyId;

    @Column(name="guid")
    protected String guid;
    
    @Column(name="creationDate")
    protected Date creationDate;

    @Column(name="phoneNumber")
    protected String phoneNumber;
    
    @Column(name="secretCode")
    protected String secretCode;
    
	@Column(name="status")
    protected String status;

}


