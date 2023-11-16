package br.com.fiap.farmacia.eficiente.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fiap.farmacia.eficiente.controllers.PostoController;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @OneToOne
    private Endereco endereco;

    @Size(min = 1, max = 50, message = "O nome do posto de saude deve ter entre 1 e 50 caracteres")
    @NotNull 
    private String nome;

    @Size(min = 1, max = 50, message = "A descricao do posto de saude deve ter entre 1 e 50 caracteres")
    @NotNull 
    private String descricao;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

    public EntityModel<Posto> toEntityModel(){
        return EntityModel.of(
            this, 
            linkTo(methodOn(PostoController.class).show(id)).withSelfRel(),
            linkTo(methodOn(PostoController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(PostoController.class).update(this)).withRel("update")
        );
    }
}
