#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <hiredis/hiredis.h>
#include <QMessageBox>

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// \brief MainWindow::on_pushButton_clicked
///
///
/// redis-server   redis-cli
///
/// set key 1001
/// get key
/// del key
///
/// hiredis.h-
///         redisContext()--redisCommand--redisReply()
///
///         redisContext* redisConnect(const char* ip, int port)- connect redis database with ip and port(6379)
///
///         void* redisCommand(redisContext* c, const char* format, ...)- sent command to redis
///
///         redisReply()- echo back
/// :)
///
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

void MainWindow::on_pushButton_clicked()
{
    struct timeval timeout={2,0};
    redisContext* pRedisContext = (redisContext*)redisConnectWithTimeout("127.0.0.1",6379,timeout);

    if(pRedisContext != NULL && pRedisContext->err){
        QMessageBox::StandardButton conn_error = QMessageBox::warning(this,"error",pRedisContext->errstr);
    }

    const char* command("get key");
    redisReply* reply = (redisReply*)redisCommand(pRedisContext,command);

    QString str = reply->str;
    ui->textEdit->append(str);



    freeReplyObject(reply);
    redisFree(pRedisContext);

}


