package com.project.Mesa.Model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class campanhas implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String periodo;
	private String grupo;
	@ManyToOne
	@JoinColumn(name = "cnpj_filial", referencedColumnName = "cnpj")
	private filial empresa;
	@Column(nullable = true)
	private String categoria_participante;
	@Column(nullable = true)
	private String cargoParticipante;
	private String nomeParticipante;
	@Column(nullable = true)
	private String meta;
	@Column(nullable = true)
	private String real_volume;
	@Column(nullable = true)
	private String realizado;
	@Column(nullable = true)
	private String meta_atingida;
	@Column(nullable = true)
	private String meta_positivacao;
	@Column(nullable = true)
	private String real_positivacao;
	@Column(nullable = true)
	private String meta_positivacao_atingida;
	@Column(nullable = true)
	private String valor_por_bateria;
	@Column(nullable = true)
	private String faixa_de_premiacao;
	private String valor_premiacao;
	private String valor_com_taxa;
	@Column(nullable = true)
	private String colocacao;
	@Column(nullable = true)
	private String positivou;
	@Column(nullable = false)
	private String paginas;

	

	public String getPaginas() {
		return paginas;
	}

	public void setPaginas(String paginas) {
		this.paginas = paginas;
	}

	public String getFaixa_de_premiacao() {
		return faixa_de_premiacao;
	}

	public void setFaixa_de_premiacao(String faixa_de_premiacao) {
		this.faixa_de_premiacao = faixa_de_premiacao;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public filial getEmpresa() {
		return empresa;
	}

	public void setEmpresa(filial empresa) {
		this.empresa = empresa;
	}

	public String getCategoria_participante() {
		return categoria_participante;
	}

	public void setCategoria_participante(String categoria_participante) {
		this.categoria_participante = categoria_participante;
	}

	

	public String getCargoParticipante() {
		return cargoParticipante;
	}

	public void setCargoParticipante(String cargoParticipante) {
		this.cargoParticipante = cargoParticipante;
	}

	public String getNomeParticipante() {
		return nomeParticipante;
	}

	public void setNomeParticipante(String nomeParticipante) {
		this.nomeParticipante = nomeParticipante;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getRealizado() {
		return realizado;
	}

	public void setRealizado(String realizado) {
		this.realizado = realizado;
	}

	public String getMeta_atingida() {
		return meta_atingida;
	}

	public void setMeta_atingida(String meta_atingida) {
		this.meta_atingida = meta_atingida;
	}

	public String getValor_por_bateria() {
		return valor_por_bateria;
	}

	public void setValor_por_bateria(String valor_por_bateria) {
		this.valor_por_bateria = valor_por_bateria;
	}

	public String getValor_premiacao() {
		return valor_premiacao;
	}

	public String getReal_volume() {
		return real_volume;
	}

	public void setReal_volume(String real_volume) {
		this.real_volume = real_volume;
	}

	public String getMeta_positivacao() {
		return meta_positivacao;
	}

	public void setMeta_positivacao(String meta_positivacao) {
		this.meta_positivacao = meta_positivacao;
	}

	public String getReal_positivacao() {
		return real_positivacao;
	}

	public void setReal_positivacao(String real_positivacao) {
		this.real_positivacao = real_positivacao;
	}

	public String getMeta_positivacao_atingida() {
		return meta_positivacao_atingida;
	}

	public void setMeta_positivacao_atingida(String meta_positivacao_atingida) {
		this.meta_positivacao_atingida = meta_positivacao_atingida;
	}

	public void setValor_premiacao(String valor_premiacao) {
		this.valor_premiacao = valor_premiacao;
	}

	public String getValor_com_taxa() {
		return valor_com_taxa;
	}

	public void setValor_com_taxa(String valor_com_taxa) {
		this.valor_com_taxa = valor_com_taxa;
	}

	public String getColocacao() {
		return colocacao;
	}

	public void setColocacao(String colocacao) {
		this.colocacao = colocacao;
	}

	public String getPositivou() {
		return positivou;
	}

	public void setPositivou(String positivou) {
		this.positivou = positivou;
	}


}
