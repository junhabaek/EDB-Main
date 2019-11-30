package org.edb.main.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.edb.main.*;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static org.edb.main.UI.BootApp.primaryStage;

public class PluginConfigUIController implements Initializable {

    @FXML
    private VBox configArea;
    @FXML
    private AnchorPane applyArea;
    @FXML
    private TextField startHourField;
    @FXML
    private TextField startMinField;
    @FXML
    private TextField endHourField;
    @FXML
    private TextField endMinField;
    @FXML
    private TableView<FXTargetProgram> targetProgramTableView;
    @FXML
    private TableView<FXTargetWebsite> targetWebsiteTableView;
    @FXML
    private TableColumn<FXTargetProgram, String> targetProgramNameColumn;
    @FXML
    private TableColumn<FXTargetProgram, String> targetProgramPathColumn;
    @FXML
    private TableColumn<FXTargetWebsite, String> targetWebsiteURLColumn;

    private EDBPlugin plugin;
    private EDBPluginManager pluginManager;
    private UIEventHandler uiEventHandler;
    private FXTargetProgram curSelectedProgram;
    private FXTargetWebsite curSelectedWebsite;
    private ObservableList<FXTargetProgram> fxTargetProgramObservableList = FXCollections.observableArrayList();
    private ObservableList<FXTargetWebsite> fxTargetWebsiteObservableList = FXCollections.observableArrayList();

    public void setPluginManager(EDBPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void setUIEventHandler(UIEventHandler uiEventHandler) {
        this.uiEventHandler=uiEventHandler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        targetProgramNameColumn.setCellValueFactory(cellData->cellData.getValue().targetNameProperty());
        targetProgramPathColumn.setCellValueFactory(cellData->cellData.getValue().targetPathProperty());
        targetWebsiteURLColumn.setCellValueFactory(cellData->cellData.getValue().targetURLProperty());

        targetProgramTableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onProgramSelected(newValue));
        targetWebsiteTableView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> onWebsiteSelected(newValue));

        targetProgramTableView.setItems(fxTargetProgramObservableList);
        targetWebsiteTableView.setItems(fxTargetWebsiteObservableList);
    }

    public void addTargetProgram(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        FXTargetProgram addedTargetProgram = new FXTargetProgram(selectedFile.getName(),selectedFile.getAbsolutePath());

        fxTargetProgramObservableList.add(addedTargetProgram);

        plugin.addTargetProgram(addedTargetProgram.convertToTargetProgram());
    }

    public void deleteTargetProgram(){

//        curSelectedProgram 참고해서 삭제하기

//        테이블 뷰에서 삭제
//        EDBPluginConfig에서도 삭제
    }

    public void addTargetWebsite(){
//        textField에 입력된값 기반
    }

    public void deleteTargetWebsite(){
//        curSelectedWebsite 참고해서 삭제하기
    }

    public void applyToServer(){
//        edbPlugin변환해서 서버리퀘스트로 보낸다
    }

    public void schedulePlugin(){
//        textField참고해서 설정값에 넣는다.

    }

    public void onProgramSelected(FXTargetProgram selectedProgram){
        curSelectedProgram = selectedProgram;
    }

    public void onWebsiteSelected(FXTargetWebsite selectedWebsite){
        curSelectedWebsite = selectedWebsite;
    }

/*
        //1.  잠금정책목록 불러오기
        // 2. 잠금정책등록 선택시 추가가능한 잠금정책목록보여주기
        // 3. 잠금정책목록에서 특정 잠금 정책 클릭 시 잠금정책설정 불러오기
        // 3. 잠금정책목록에서 특정 잠금 정책 화면에서 잠금정책설정 저장

    PluginConfigUI Class: Parent (Container) 상속), initilaizable interface

    각 플러그 configui controller가
    연결되는 plugin클래스에 대한 참조를 가지고 있어서
    서버 반영버튼 누르면 해당 플러그인에 대해서 config추출이라는 메소드 호출함
    각 플러그인들은 내부에 SpecificConfig라는 내부클래스 가지고 있어서 거기에 설정정보들 저장되고, 서버에 저장되는 스트링형태로 변환하는 메소드 가짐
    config 추출의 결과가 나오면 그거 일단 사용자 디바이스에 파일형태로 저장하고(구현안할수도 있음), 그담에 restpaiconnector 이용해서 서버에 저장하면됨
    아 이러면 한꺼번에 전달되는게
    안되는구나
    하나의 설정을 저장할때마다 전체 플러그인 컨피그파일을 다시만들어 서버에 올리거나
    pluginid마다 pluginconfig를 서버가 따로 관리하도록 하거나
    둘중 하나로 결정해야할것같네
    후자의 경우 useridx, pluginidx, 잠금시간, pluginconfig로 구성되는 테이블이 될듯

    명다연 7:46 PM
    아하 그렇다면 나는 서버에 useridx,plugidx,잠금시간,pluginconfig정보를 등록하는 retrofit만 짜면 되는구나
    굉장히 간단한거엿네

    u13은?? :일단 설정불러오기는 로그인됐을때 자동으로 수행되는걸로 가정했어
    User에 로그인 메서드가 실행되면 그 안에서 EDBPluginManager에게 플러그인 갱신해달라고 시키고
    EDBPluginManager는 서버와 통신하면서 유저에게 등록된 플러그인 설정 목록들을 전부 받아와
    그거를 각 플러그인한테 보내서 설정에 반영하도록 시키는내용
 */



}
