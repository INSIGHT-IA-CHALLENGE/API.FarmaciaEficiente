package br.com.fiap.farmacia.eficiente.models;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.fiap.farmacia.eficiente.controllers.MedicamentoController;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 1, max = 50, message = "O nome do medicamento deve ter entre 1 e 50 caracteres")
    @NotNull 
    private String nome;

    @Size(min = 1, max = 50, message = "O nome do fabricante deve ter entre 1 e 50 caracteres")
    @NotNull 
    private String fabricante;

    @Size(min = 1, max = 10, message = "A descrição da dosagem deve ter entre 1 e 10 caracteres")
    @NotNull 
    private String dosagem;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    private Status status = Status.ATIVO;

     public EntityModel<Medicamento> toEntityModel(){
        return EntityModel.of(
            this, 
            linkTo(methodOn(MedicamentoController.class).show(id)).withSelfRel(),
            linkTo(methodOn(MedicamentoController.class).destroy(id)).withRel("delete"),
            linkTo(methodOn(MedicamentoController.class).update(this)).withRel("update")
        );
    }
}
