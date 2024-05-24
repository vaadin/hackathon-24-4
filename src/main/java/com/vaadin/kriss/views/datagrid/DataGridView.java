package com.vaadin.kriss.views.datagrid;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@PageTitle("Data Grid")
@Menu(icon = "line-awesome/svg/th-solid.svg", order = 2)
@Route(value = "data-grid")
public class DataGridView extends Div {

    private GridPro<Client> grid;
    private GridListDataView<Client> gridListDataView;

    private Grid.Column<Client> clientColumn;
    private Grid.Column<Client> amountColumn;
    private Grid.Column<Client> statusColumn;
    private Grid.Column<Client> dateColumn;

    private boolean scrollingAllowed = false;
    private ExecutorService executor;
    private final Random random = new Random();
    private boolean someFlag = false;

    public DataGridView() {
        addClassName("data-grid-view");
        setSizeFull();
        createGrid();
        add(createSomeCheckbox());
        add(grid);
    }

    private Checkbox createSomeCheckbox() {
        Checkbox someCheckbox = new Checkbox("Some Checkbox");
        someCheckbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                someCheckbox.setHelperComponent(null);
                someCheckbox.setHelperText("This really helps!");
            } else {
                someCheckbox.setHelperText(null);
                someCheckbox.setHelperComponent(VaadinIcon.HEART_O.create());
            }
        });
//        someCheckbox.addDoubleClickListener(doubleClickEvent -> {
////            someCheckbox.setHelperText(null);
//            someCheckbox.setHelperComponent(VaadinIcon.HEART.create());
//        });
        return someCheckbox;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        scrollingAllowed = true;
        startScrollingToRow();
    }

    private void startScrollingToRow() {
        executor = Executors.newVirtualThreadPerTaskExecutor();

        executor.submit(() -> {
            while (scrollingAllowed) {
                try {
                    System.out.println("Running task in a virtual thread: " + Thread.currentThread().getName());
                    Thread.sleep(3000);
                    getUI().ifPresent(ui -> ui.access(() -> {
                        Client item = grid.getListDataView().getItem(random.nextInt(0, grid.getListDataView().getItemCount()));
                        grid.scrollToItem(item);
                        grid.select(item);
                        ui.push();
                    }));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        scrollingAllowed = false;
        System.out.println("Detach triggered");

        executor.shutdown();
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<Client> clients = getClients();
        gridListDataView = grid.setItems(clients);
    }

    private void addColumnsToGrid() {
        createClientColumn();
        createAmountColumn();
        createStatusColumn();
        createDateColumn();
    }

    private void createClientColumn() {
        clientColumn = grid.addColumn(new ComponentRenderer<>(client -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Image img = new Image(client.getImg(), "");
            Span span = new Span();
            span.setClassName("name");
            span.setText(client.getClient());
            hl.add(img, span);
            return hl;
        })).setComparator(client -> client.getClient()).setHeader("Client");
    }

    private void createAmountColumn() {
        amountColumn = grid.addEditColumn(Client::getAmount, new NumberRenderer<>(client -> client.getAmount(), NumberFormat.getCurrencyInstance(Locale.US))).text((item, newValue) -> item.setAmount(Double.parseDouble(newValue))).setComparator(client -> client.getAmount()).setHeader("Amount");
    }

    private void createStatusColumn() {
        var statusColumnConf = grid.addEditColumn(Client::getClient, new ComponentRenderer<>(client -> {
            Span span = new Span();
            span.setText(client.getStatus());
            span.getElement().setAttribute("theme", "badge " + client.getStatus().toLowerCase());
            return span;
        }));
        statusColumn = statusColumnConf.getColumn();
        statusColumnConf.select((item, newValue) -> item.setStatus(newValue), Arrays.asList("Pending", "Success", "Error")).setComparator(client -> client.getStatus()).setHeader("Status");
        statusColumnConf.withCellEditableProvider(client -> client.getStatus().equals("Pending"));
    }

    private void createDateColumn() {
        dateColumn = grid.addColumn(new LocalDateRenderer<>(client -> LocalDate.parse(client.getDate()), () -> DateTimeFormatter.ofPattern("M/d/yyyy"))).setComparator(client -> client.getDate()).setHeader("Date").setWidth("180px").setFlexGrow(0);
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField clientFilter = new TextField();
        clientFilter.setPlaceholder("Filter");
        clientFilter.setClearButtonVisible(true);
        clientFilter.setWidth("100%");
        clientFilter.setValueChangeMode(ValueChangeMode.EAGER);
        clientFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> StringUtils.containsIgnoreCase(client.getClient(), clientFilter.getValue())));
        filterRow.getCell(clientColumn).setComponent(clientFilter);

        TextField amountFilter = new TextField();
        amountFilter.setPlaceholder("Filter");
        amountFilter.setClearButtonVisible(true);
        amountFilter.setWidth("100%");
        amountFilter.setValueChangeMode(ValueChangeMode.EAGER);
        amountFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> StringUtils.containsIgnoreCase(Double.toString(client.getAmount()), amountFilter.getValue())));
        filterRow.getCell(amountColumn).setComponent(amountFilter);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("Pending", "Success", "Error"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> areStatusesEqual(client, statusFilter)));
        filterRow.getCell(statusColumn).setComponent(statusFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(event -> gridListDataView.addFilter(client -> areDatesEqual(client, dateFilter)));
        filterRow.getCell(dateColumn).setComponent(dateFilter);
    }

    private boolean areStatusesEqual(Client client, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null) {
            return StringUtils.equals(client.getStatus(), statusFilterValue);
        }
        return true;
    }

    private boolean areDatesEqual(Client client, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate clientDate = LocalDate.parse(client.getDate());
            return dateFilterValue.equals(clientDate);
        }
        return true;
    }

    private List<Client> getClients() {
        return Arrays.asList(createClient(4957, "https://randomuser.me/api/portraits/women/42.jpg", "Amarachi Nkechi", 47427.0, "Success", "2019-05-09"), createClient(675, "https://randomuser.me/api/portraits/women/24.jpg", "Bonelwa Ngqawana", 70503.0, "Success", "2019-05-09"), createClient(6816, "https://randomuser.me/api/portraits/men/42.jpg", "Debashis Bhuiyan", 58931.0, "Success", "2019-05-07"), createClient(5144, "https://randomuser.me/api/portraits/women/76.jpg", "Jacqueline Asong", 25053.0, "Pending", "2019-04-25"), createClient(9800, "https://randomuser.me/api/portraits/men/24.jpg", "Kobus van de Vegte", 7319.0, "Pending", "2019-04-22"), createClient(3599, "https://randomuser.me/api/portraits/women/94.jpg", "Mattie Blooman", 18441.0, "Error", "2019-04-17"), createClient(3989, "https://randomuser.me/api/portraits/men/76.jpg", "Oea Romana", 33376.0, "Pending", "2019-04-17"), createClient(1077, "https://randomuser.me/api/portraits/men/94.jpg", "Stephanus Huggins", 75774.0, "Success", "2019-02-26"), createClient(8942, "https://randomuser.me/api/portraits/men/16.jpg", "Torsten Paulsson", 82531.0, "Pending", "2019-02-21"), createClient(4000, "https://randomuser.me/api/portraits/men/45.jpg", "John Doe", 30000.0, "Pending", "2019-05-01"), createClient(4001, "https://randomuser.me/api/portraits/men/46.jpg", "Jane Smith", 32000.0, "Pending", "2019-05-02"), createClient(4002, "https://randomuser.me/api/portraits/men/47.jpg", "Alice Johnson", 31000.0, "Pending", "2019-05-03"), createClient(4003, "https://randomuser.me/api/portraits/men/48.jpg", "Bob Brown", 30500.0, "Pending", "2019-05-04"), createClient(4004, "https://randomuser.me/api/portraits/men/49.jpg", "Charlie Davis", 29500.0, "Pending", "2019-05-05"), createClient(4005, "https://randomuser.me/api/portraits/men/50.jpg", "David Evans", 33000.0, "Pending", "2019-05-06"), createClient(4006, "https://randomuser.me/api/portraits/men/51.jpg", "Eve Foster", 34000.0, "Pending", "2019-05-07"), createClient(4007, "https://randomuser.me/api/portraits/men/52.jpg", "Frank Green", 34500.0, "Pending", "2019-05-08"), createClient(4008, "https://randomuser.me/api/portraits/men/53.jpg", "Grace Harris", 35000.0, "Pending", "2019-05-09"), createClient(4009, "https://randomuser.me/api/portraits/men/54.jpg", "Henry Jackson", 36000.0, "Pending", "2019-05-10"), createClient(4010, "https://randomuser.me/api/portraits/men/55.jpg", "Ivy King", 37000.0, "Pending", "2019-05-11"), createClient(4011, "https://randomuser.me/api/portraits/men/56.jpg", "Jack Lewis", 38000.0, "Pending", "2019-05-12"), createClient(4012, "https://randomuser.me/api/portraits/men/57.jpg", "Karen Martin", 39000.0, "Pending", "2019-05-13"), createClient(4013, "https://randomuser.me/api/portraits/men/58.jpg", "Leo Nelson", 40000.0, "Pending", "2019-05-14"), createClient(4014, "https://randomuser.me/api/portraits/men/59.jpg", "Mia Owen", 41000.0, "Pending", "2019-05-15"), createClient(4015, "https://randomuser.me/api/portraits/men/60.jpg", "Nina Perry", 42000.0, "Pending", "2019-05-16"), createClient(4016, "https://randomuser.me/api/portraits/men/61.jpg", "Oscar Quinn", 43000.0, "Pending", "2019-05-17"), createClient(4017, "https://randomuser.me/api/portraits/men/62.jpg", "Paul Roberts", 44000.0, "Pending", "2019-05-18"), createClient(4018, "https://randomuser.me/api/portraits/men/63.jpg", "Quinn Scott", 45000.0, "Pending", "2019-05-19"), createClient(4019, "https://randomuser.me/api/portraits/men/64.jpg", "Rose Taylor", 46000.0, "Pending", "2019-05-20"));

    }

    private Client createClient(int id, String img, String client, double amount, String status, String date) {
        Client c = new Client();
        c.setId(id);
        c.setImg(img);
        c.setClient(client);
        c.setAmount(amount);
        c.setStatus(status);
        c.setDate(date);

        return c;
    }
}
