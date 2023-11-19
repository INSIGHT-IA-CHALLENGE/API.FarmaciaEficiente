package br.com.fiap.farmacia.eficiente.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fiap.farmacia.eficiente.models.Endereco;
import br.com.fiap.farmacia.eficiente.models.Estoque;
import br.com.fiap.farmacia.eficiente.models.Medicamento;
import br.com.fiap.farmacia.eficiente.models.Posto;
import br.com.fiap.farmacia.eficiente.models.Retirada;
import br.com.fiap.farmacia.eficiente.models.TipoUsuario;
import br.com.fiap.farmacia.eficiente.models.Usuario;
import br.com.fiap.farmacia.eficiente.repository.EnderecoRepository;
import br.com.fiap.farmacia.eficiente.repository.EstoqueRepository;
import br.com.fiap.farmacia.eficiente.repository.MedicamentoRepository;
import br.com.fiap.farmacia.eficiente.repository.PostoRepository;
import br.com.fiap.farmacia.eficiente.repository.RetiradaRepository;
import br.com.fiap.farmacia.eficiente.repository.UsuarioRepository;

@Configuration
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner{

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PostoRepository postoRepository;

    @Autowired
    MedicamentoRepository medicamentoRepository;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    RetiradaRepository retiradaRepository;
    

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        
        Endereco endereco1 = new Endereco();
        endereco1.setLogradouro("Rua Evaldo Calabrez");
        endereco1.setBairro("Vila Princesa Isabel");
        endereco1.setNumero("1804");
        endereco1.setCidade("São Paulo");
        endereco1.setUf("SP");
        endereco1.setCep("08410-070");
        enderecoRepository.save(endereco1);

        Endereco endereco2 = new Endereco();
        endereco2.setLogradouro("Rua Comandante Carlos Ruhl");
        endereco2.setBairro("Vila Princesa Isabel");
        endereco2.setNumero("189");
        endereco2.setCidade("São Paulo");
        endereco2.setUf("SP");
        endereco2.setCep("08410-130");
        enderecoRepository.save(endereco2);

        Endereco endereco3 = new Endereco();
        endereco3.setLogradouro("Viaduto do Chá");
        endereco3.setBairro("Centro");
        endereco3.setNumero("15");
        endereco3.setCidade("São Paulo");
        endereco3.setUf("SP");
        endereco3.setCep("01002-020");
        enderecoRepository.save(endereco3);

        Usuario usuario1 = new Usuario();
        usuario1.setNome("Gustavo Balero");
        usuario1.setEmail("rm93090@fiap.com.br");
        usuario1.setSenha(encoder.encode("abc123"));
        usuario1.setTipoUsuario(TipoUsuario.PACIENTE);
        usuario1.setTelefone("(11) 99504-9078");
        usuario1.setEndereco(endereco1);
        usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Prefeitura de São Paulo");
        usuario2.setEmail("sp@prefeitura.gov.br");
        usuario2.setSenha(encoder.encode("abc123"));
        usuario2.setTipoUsuario(TipoUsuario.ADMIN);
        usuario2.setTelefone("(11) 3243-1008");
        usuario2.setEndereco(endereco3);
        usuarioRepository.save(usuario2);

        Posto posto = new Posto();
        posto.setNome("UBS GUAIANASES II");
        posto.setDescricao("Funcionamento de segunda a sexta das  8h as 18h");
        posto.setEndereco(endereco2);
        postoRepository.save(posto);

        Medicamento medicamento1 = new Medicamento();
        medicamento1.setNome("Dipirona");
        medicamento1.setDosagem("500mg");
        medicamento1.setFabricante("Medley");
        medicamentoRepository.save(medicamento1);

        Medicamento medicamento2 = new Medicamento();
        medicamento2.setNome("Paracetamol");
        medicamento2.setDosagem("500mg");
        medicamento2.setFabricante("Neo Química");
        medicamentoRepository.save(medicamento2);

        Estoque estoque1 = new Estoque();
        estoque1.setMedicamento(medicamento1);
        estoque1.setPosto(posto);
        estoque1.setQuantidade(99);
        estoqueRepository.save(estoque1);

        Estoque estoque2 = new Estoque();
        estoque2.setMedicamento(medicamento2);
        estoque2.setPosto(posto);
        estoque2.setQuantidade(100);
        estoqueRepository.save(estoque2);

        Retirada retirada1 = new Retirada();
        retirada1.setEstoque(estoque1);
        retirada1.setUsuario(usuario1);
        retiradaRepository.save(retirada1);
    }
}
