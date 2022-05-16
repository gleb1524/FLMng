package ru.gb.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ru.gb.dto.*;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BasicHandler extends ChannelInboundHandlerAdapter {

    DataBaseService dataBaseService = new DataBaseService();
    private  String login;
    private  String password;
    private  String auth;
    private final String SER_DIR = ".";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(msg instanceof FileRequest){
            FileRequest request = (FileRequest) msg;
            if(dataBaseService.hasAuthRegister(request.getAuth(), request.getLogin())){
                System.out.println(request.getPath());
                Path path = Paths.get(request.getPath());
                if(!Files.exists(path)){
                    ctx.writeAndFlush(new BasicResponse("creat_ok"));
                    Files.createDirectory(path);
                }
            }
        }

        if(msg instanceof UploadRequest){
            UploadRequest request = (UploadRequest) msg;
            String pathOfFile = String.format(((UploadRequest) request).getRemPath()+"\\%s",((UploadRequest)request).getFilename());
            System.out.println(pathOfFile);
            try (FileOutputStream fos = new FileOutputStream(pathOfFile)) {
                fos.write(( request).getData());
            }
        }
        if(msg instanceof RegRequest){
            RegRequest request = (RegRequest) msg;
            if(!dataBaseService.hasRegistration(request.getLogin())){
                dataBaseService.creatRegistration(request.getLogin(), request.getPassword(),
                        request.getName(), request.getSurname());
                ctx.writeAndFlush(new BasicResponse("reg_ok"));
            }else{
                ctx.writeAndFlush(new BasicResponse("reg_no"));
            }
        }else if(msg instanceof AuthRequest) {
            AuthRequest request = (AuthRequest) msg;
            login = request.getLogin();
            password = request.getPassword();
            auth = dataBaseService.creatAuth(request.getLogin(), request.getPassword());
            if(!dataBaseService.isAuthRegister(auth,login)){
                dataBaseService.authRegister(auth,login);
            }
            ctx.writeAndFlush(new BasicResponse("auth "+ auth));
            System.out.println(auth);
            if(dataBaseService.hasAuth(login, password)){
                String filename = SER_DIR + "\\" +login+"root";
                Path path = Paths.get(filename);
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                    System.out.println("New Directory created !   "+filename);
                } else {
                    System.out.println("Directory already exists");
                }
                ctx.writeAndFlush(new BasicResponse("server_dir " + filename));
                ctx.writeAndFlush(new BasicResponse("auth_ok"));
            }else{
                ctx.writeAndFlush(new BasicResponse("auth_no"));
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
