package it.etoken.base.model.eosblock.entity;

import java.io.Serializable;

public class Delegatebw implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1332350758449878352L;

	/**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column delegatebw.id
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column delegatebw.account_name
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    private String accountName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column delegatebw.status
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    private Long status;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column delegatebw.cpu
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    private String cpu;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column delegatebw.net
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    private String net;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column delegatebw.id
     *
     * @return the value of delegatebw.id
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column delegatebw.id
     *
     * @param id the value for delegatebw.id
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column delegatebw.account_name
     *
     * @return the value of delegatebw.account_name
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column delegatebw.account_name
     *
     * @param accountName the value for delegatebw.account_name
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column delegatebw.status
     *
     * @return the value of delegatebw.status
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public Long getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column delegatebw.status
     *
     * @param status the value for delegatebw.status
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public void setStatus(Long status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column delegatebw.cpu
     *
     * @return the value of delegatebw.cpu
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public String getCpu() {
        return cpu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column delegatebw.cpu
     *
     * @param cpu the value for delegatebw.cpu
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column delegatebw.net
     *
     * @return the value of delegatebw.net
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public String getNet() {
        return net;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column delegatebw.net
     *
     * @param net the value for delegatebw.net
     *
     * @mbg.generated Tue Aug 14 17:25:29 CST 2018
     */
    public void setNet(String net) {
        this.net = net;
    }
}