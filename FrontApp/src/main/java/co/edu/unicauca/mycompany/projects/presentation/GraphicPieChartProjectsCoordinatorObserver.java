package co.edu.unicauca.mycompany.projects.presentation;

import co.edu.unicauca.mycompany.projects.domain.entities.Coordinator;
import co.edu.unicauca.mycompany.projects.infra.Observer;
import co.edu.unicauca.mycompany.projects.domain.services.ProjectService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.Color;

import javax.swing.*;
import java.util.List;

/**
 * Observador que muestra una gráfica de pastel con los estados de los proyectos
 * del coordinador.
 */
public class GraphicPieChartProjectsCoordinatorObserver extends JFrame implements Observer {

    private final ProjectService projectService;
    private final Coordinator coordinator;
    /**
     * Panel donde se renderizará la gráfica de proyectos.
     */
    private final JPanel jPanelGrafico;

    /**
     * Constructor que recibe los datos necesarios para construir la gráfica.
     *
     * @param coordinator Coordinador cuyos proyectos se van a graficar
     * @param projectService Servicio para obtener los datos de los proyectos
     */
    public GraphicPieChartProjectsCoordinatorObserver(Coordinator coordinator, ProjectService projectService, JPanel jPanelGrafico) {
        this.coordinator = coordinator;
        this.jPanelGrafico = jPanelGrafico;
        this.projectService = projectService;

        setTitle("Gráfica de Proyectos - Pastel");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        configurarGrafica();
    }

    /**
     * Crea y muestra la gráfica de pastel con los datos actuales.
     */
    private void configurarGrafica() {
        List<Integer> datos = projectService.dataGraphicCoordinator();

        // Crear el dataset con los mismos estados que la gráfica de barras
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("RECIBIDO", datos.get(0));
        dataset.setValue("ACEPTADO", datos.get(1));
        dataset.setValue("RECHAZADO", datos.get(2));
        dataset.setValue("EJECUCIÓN", datos.get(3));
        dataset.setValue("CERRADO", datos.get(4));

        // Crear el gráfico
        JFreeChart chart = ChartFactory.createPieChart(
                "Cantidad de Proyectos por Estado",
                dataset,
                true, true, false);

        var plot = (org.jfree.chart.plot.PiePlot) chart.getPlot();
        plot.setLabelGenerator(null); // Esto elimina las etiquetas flotantes del gráfico
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        // Asignar colores personalizados
        plot.setSectionPaint("RECIBIDO", new Color(95, 232, 172));
        plot.setSectionPaint("ACEPTADO", new Color(95, 232, 125));
        plot.setSectionPaint("RECHAZADO", new Color(95, 232, 218));
        plot.setSectionPaint("EJECUCIÓN", new Color(95, 159, 232));
        plot.setSectionPaint("CERRADO", new Color(172, 232, 226));
        
        // Panel con el gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(jPanelGrafico.getWidth(), jPanelGrafico.getHeight());
        jPanelGrafico.add(chartPanel);
    }

    /**
     * Actualiza la gráfica cuando cambian los datos.
     */
    @Override
    public void update() {
        jPanelGrafico.removeAll(); // Limpiar el panel gráfico
        configurarGrafica(); // Volver a generar la gráfica con los datos actualizados
        jPanelGrafico.revalidate(); // Validar los cambios en la interfaz
        jPanelGrafico.repaint(); // Repintar la interfaz gráfica
    }
}
