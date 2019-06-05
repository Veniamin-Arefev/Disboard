package ru.veniamin_arefev.disboard.configs;
// Created by Veniamin_arefev
// Date was 12.05.2019

import ru.veniamin_arefev.disboard.Disboard;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyFileVisitor extends SimpleFileVisitor {
    private Path source;
    private Path target;
    private StandardCopyOption copyOption;

    public CopyFileVisitor(Path source, Path target, StandardCopyOption copyOption) {
        this.source = source;
        this.target = target;
        this.copyOption = copyOption;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {

        Path newDir = target.resolve(source.relativize((Path) dir));
        try {
            Files.copy((Path) dir, newDir, copyOption);
        } catch (IOException e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                Disboard.logger.error("Cant create default loot tables",e.fillInStackTrace());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {

        Path newDir = target.resolve(source.relativize((Path) file));
        try {
            Files.copy((Path) file, newDir, copyOption);
        } catch (IOException e) {
            if (!(e instanceof FileAlreadyExistsException)) {
                Disboard.logger.error("Cant create default loot tables",e.fillInStackTrace());
            }
        }
        return FileVisitResult.CONTINUE;
    }

}
