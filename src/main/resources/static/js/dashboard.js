const sidebar = document.querySelector(".sidebar");
const sidebarOpenBtn = document.querySelector("#sidebar-open");
const sidebarCloseBtn = document.querySelector("#sidebar-close");
const sidebarLockBtn = document.querySelector("#lock-icon");
const sidebarClosedIcon = document.querySelector("#sidebar-closed-icon");

const toggleLock = () => {
  sidebar.classList.toggle("locked");
  if (!sidebar.classList.contains("locked")) {
    sidebar.classList.add("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-alt", "bx-lock-open-alt");
  } else {
    sidebar.classList.remove("hoverable");
    sidebarLockBtn.classList.replace("bx-lock-open-alt", "bx-lock-alt");
  }
};

const hideSidebar = () => {
  if (sidebar.classList.contains("hoverable")) {
    sidebar.classList.add("close");
    sidebarCloseBtn.style.display = "block";
    sidebarClosedIcon.style.display = "none";
  }
};

const showSidebar = () => {
  if (sidebar.classList.contains("hoverable")) {
    sidebar.classList.remove("close");
    sidebarCloseBtn.style.display = "none";
    sidebarClosedIcon.style.display = "block";
  }
};

const toggleSidebar = () => {
  sidebar.classList.toggle("close");

  if (sidebar.classList.contains("close")) {
    sidebarCloseBtn.style.display = "block";
    sidebarClosedIcon.style.display = "none";
  } else {
    sidebarCloseBtn.style.display = "none";
    sidebarClosedIcon.style.display = "block";
  }
};

sidebarLockBtn.addEventListener("click", toggleLock);
sidebar.addEventListener("mouseleave", hideSidebar);
sidebar.addEventListener("mouseenter", showSidebar);
sidebarCloseBtn.addEventListener("click", toggleSidebar);
sidebarClosedIcon.addEventListener("click", toggleSidebar);

if (window.innerWidth < 800) {
  sidebar.classList.add("close");
  sidebar.classList.remove("locked");
  sidebar.classList.remove("hoverable");
}

var metaTotal = /*[[${metaTotal}]]*/ 0;

// Captura os dados da tabela
var participantes = [];
var metas = [];
var metasRealizadas = [];
var premiacao = [];

// Captura os dados da tabela
document.querySelectorAll("#comidasPremiadas tbody tr").forEach(function (row) {
  var cols = row.querySelectorAll("td");
  participantes.push(cols[0].innerText); // Participante (coluna 0)
  metas.push(parseInt(cols[1].innerText)); // Meta (coluna 1)
  metasRealizadas.push(parseInt(cols[2].innerText)); // Realizada (coluna 2)
  premiacaoString = cols[3].innerText
    .replace("R$", "")
    .replace(/\./g, "")
    .replace(",", ".");
  premiacao.push(parseFloat(premiacaoString));
});

console.log(participantes);
console.log(metas);
console.log(metasRealizadas);
console.log(premiacao);

var ctx = document.getElementById("comidaPreferida_bar").getContext("2d");
var comidaPreferida_bar = new Chart(ctx, {
  type: "bar",
  data: {
    labels: participantes,
    datasets: [
      {
        label: "Meta",
        data: metas,
        backgroundColor: "#C21712",
        borderColor: "#C21712",
        borderWidth: 1,
      },
      {
        label: "Meta Realizada",
        data: metasRealizadas,
        backgroundColor: "#583F99",
        borderColor: "#583F99",
        borderWidth: 1,
      },
    ],
  },
  options: {
    indexAxis: "y",
    scales: {
      x: {
        beginAtZero: true,
        ticks: {
          color: "rgba(0,0,0,1)",
          font: { size: 14 },
        },
      },
      y: {
        ticks: {
          color: "rgba(0,0,0,1)",
          font: { size: 14 },
        },
      },
    },
    plugins: {
      tooltip: {
        callbacks: {
          label: function (tooltipItem) {
            return tooltipItem.dataset.label + ": " + tooltipItem.raw;
          },
        },
      },
    },
  },
});

var ctxBubble = document
  .getElementById("comidaPreferida_Bolhas")
  .getContext("2d");
const dataBubble = {
  datasets: [
    {
      label: "Participantes",
      data: [
        {
          x: 1,
          y: premiacao[0],
          r: premiacao[0] / 18,
          nome: participantes[0],
          premiacao: premiacao[0],
        },
        {
          x: 2,
          y: premiacao[1],
          r: premiacao[1] / 18,
          nome: participantes[1],
          premiacao: premiacao[1],
        },
        {
          x: 3,
          y: premiacao[2],
          r: premiacao[2] / 18,
          nome: participantes[2],
          premiacao: premiacao[2],
        },
      ],
      backgroundColor: "#241F4A",
      borderColor: "#241F4A",
      borderWidth: 1,
    },
  ],
};

const configBubble = {
  type: "bubble",
  data: dataBubble,
  options: {
    scales: {
      x: {
        title: {
          display: true,
          text: "Participantes",
          color: "black",
          font: { size: 14 },
        },
        ticks: {
          color: "black",
          callback: function (value) {
            const nomes = participantes;
            return nomes[value - 1];
          },
        },
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Valor da Premiação",
          color: "black",
          font: { size: 14 },
        },
        ticks: {
          color: "black",
          font: { size: 14 },
        },
      },
    },
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context) {
            return context.raw.nome + ": R$" + context.raw.premiacao;
          },
        },
      },
    },
  },
};

var comidaPreferida_Bolhas = new Chart(ctxBubble, configBubble);

// Captura os dados da tabela
var participantesMelhoresEmpresas = [];
var metasMelhoresEmpresas = [];
var metasRealizadasMelhoresEmpresas = [];

// Captura os dados da tabela
document
  .querySelectorAll("#melhoresEmpresaDados tbody tr")
  .forEach(function (row) {
    var cols = row.querySelectorAll("td");
    participantesMelhoresEmpresas.push(cols[0].innerText); // Participante (coluna 0)
    metasMelhoresEmpresas.push(parseInt(cols[1].innerText)); // Meta (coluna 1)
    metasRealizadasMelhoresEmpresas.push(parseInt(cols[2].innerText)); // Realizada (coluna 2)
  });

console.log(participantesMelhoresEmpresas);
console.log(metasMelhoresEmpresas);
console.log(metasRealizadasMelhoresEmpresas);

var ctx = document.getElementById("melhoresEmpresas_bar").getContext("2d");

const data = {
  labels: participantesMelhoresEmpresas,
  datasets: [
    {
      label: "Meta",
      data: metasMelhoresEmpresas,
      backgroundColor: "#FF3300", // Cor para "Meta"
      borderColor: "#FF3300",
      borderWidth: 1,
    },
    {
      label: "Meta Realizada",
      data: metasRealizadasMelhoresEmpresas,
      backgroundColor: "#583F99", // Cor para "Meta Realizada"
      borderColor: "#583F99",
      borderWidth: 1,
    },
  ],
};

const config = {
  type: "bar",
  data: data,
  options: {
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context) {
            let label = context.dataset.label || "";
            if (label) {
              label += ": ";
            }
            label += context.raw;
            return label;
          },
        },
      },
    },
    responsive: true,
    scales: {
      x: {
        title: {
          display: true,
          text: "Participantes",
          color: "black",
          font: 14,
        },
        ticks: {
          color: "black",
          font: 14,
        },
        barPercentage: 0.4, // Ajusta o tamanho da barra para não ficarem tão grossas
        categoryPercentage: 0.4, // Ajusta a largura das categorias
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Valores",
          color: "black",
          font: 12,
        },
        ticks: {
          color: "black",
          font: 12,
        },
      },
    },
  },
};
var melhoresEmpresas_bar = new Chart(ctx, config);

// Captura os dados da tabela
var participantesComeMelhor = [];
var metasComeMelhor = [];
var metasRealizadasComeMelhor = [];
var positivadaComeMelhor = [];
var realPositivadaComeMelhor = [];

// Captura os dados da tabela
document.querySelectorAll("#comeMelhorDados tbody tr").forEach(function (row) {
  var cols = row.querySelectorAll("td");
  participantesComeMelhor.push(cols[0].innerText); // Participante (coluna 0)
  metasComeMelhor.push(parseInt(cols[1].innerText)); // Meta (coluna 1)
  metasRealizadasComeMelhor.push(parseInt(cols[2].innerText)); // Realizada (coluna 2)
  positivadaComeMelhor.push(parseInt(cols[3].innerText)); // Meta (coluna 1)
  realPositivadaComeMelhor.push(parseInt(cols[4].innerText));
});

console.log(participantesComeMelhor);
console.log(metasComeMelhor);
console.log(metasRealizadasComeMelhor);
console.log(positivadaComeMelhor);
console.log(realPositivadaComeMelhor);

const datas = {
  labels: participantesComeMelhor,
  datasets: [
    {
      label: "Meta Volume",
      data: metasComeMelhor,
      backgroundColor: "#C21712",
    },
    {
      label: "Real Volume",
      data: metasRealizadasComeMelhor,
      backgroundColor: "#04146D",
    },
    {
      label: "Meta Positivação",
      data: positivadaComeMelhor, // null representa ausência de valor para Gabriel
      backgroundColor: "#583F99",
    },
    {
      label: "Real Positivação",
      data: realPositivadaComeMelhor,
      backgroundColor: "#00FF1A",
    },
  ],
};

const config3 = {
  type: "bar",
  data: datas,
  options: {
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context) {
            let label = context.dataset.label || "";
            if (label) {
              label += ": ";
            }
            label += context.raw;
            return label;
          },
        },
      },
    },
    responsive: true,
    scales: {
      x: {
        title: {
          display: true,
          text: "Participantes",
          color: "black",
          font: 14,
        },
        ticks: {
          color: "black",
          font: 14,
        },
        barPercentage: 0.4, // Ajusta o tamanho da barra para não ficarem tão grossas
        categoryPercentage: 0.4, // Ajusta a largura das categorias
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Valores",
          color: "black",
          font: 12,
        },
        ticks: {
          color: "black",
          font: 12,
        },
      },
    },
  },
};

const comeMelhorBar = new Chart(
  document.getElementById("comeMelhorBar"),
  config3
);

// Captura os dados da tabela
var participantesDestaque = [];
var premiacaoDestaque = [];

document.querySelectorAll("#destaqueDados tbody tr").forEach(function (row) {
  var cols = row.querySelectorAll("td");
  participantesDestaque.push(cols[0].innerText);
  premiacao = cols[1].innerText
    .replace("R$", "")
    .replace(/\./g, "")
    .replace(",", ".");
  premiacaoDestaque.push(parseFloat(premiacao));
});

console.log(participantesDestaque);
console.log(premiacaoDestaque);

// Configurando os dados
const dataMelhor = {
  labels: participantesDestaque, // Participantes
  datasets: [
    {
      label: "Valor da Premiação",
      data: premiacaoDestaque, // Valores de premiação
      backgroundColor: [
        "#A2711D", // Cor para Lucas Andrade
        "#241F4A", // Cor para Mariana Soares
        "#770B08", // Cor para Gabriel Lima
      ],
      borderColor: ["#A2711D", "#241F4A", "#770B08"],
      borderWidth: 1,
    },
  ],
};

// Configurando o gráfico
const config4 = {
  type: "bar", // Altera o tipo para 'pie' para gráfico de pizza
  data: dataMelhor,
  options: {
    indexAxis: "y",
    scales: {
      x: {
        beginAtZero: true,
        ticks: {
          color: "rgba(0,0,0,1)",
          font: { size: 14 },
        },
      },
      y: {
        ticks: {
          color: "rgba(0,0,0,1)",
          font: { size: 14 },
        },
      },
    },
    responsive: true,
    plugins: {
      tooltip: {
        callbacks: {
          label: function (context) {
            let label = context.label || "";
            if (label) {
              label += ": ";
            }
            label += `R$ ${context.raw}`;
            return label;
          },
        },
      },
      legend: {
        position: "top",
      },
    },
  },
};

// Renderizando o gráfico
const destaqueChart = new Chart(document.getElementById("destaque"), config4);
