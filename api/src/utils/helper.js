import fs from "fs";
import path from "path";
import sharp from "sharp";

/**
 * Check if file/directory exists
 */
const exists = (path, type) => {
  try {
    if (type == "file") {
      return fs.statSync(path).isFile();
    } else if (type == "dir") {
      return fs.statSync(path).isDirectory();
    }
  } catch (e) {
    return false;
  }
};
/**
 * sharp lib
 * MacOs installation 'brew install graphicsmagick'
 * linux: https://stackoverflow.com/questions/31214724/gm-conversion-issue-in-node-js
 */
const saveThumb = async (image, filename, path) => {
  const buf = fs.readFileSync(image);
  const thumbPath = `${path}/thumb`;
  try {
    if (!exists(thumbPath, "dir")) {
      fs.mkdirSync(thumbPath, "0755");
    }

    await sharp(buf).resize(300, null).toFile(`${thumbPath}/${filename}`);

    return true;
  } catch (error) {
    console.log("Error when save thumb -> ", error);
  }
};

/**
 * Save file
 */
export const saveFile = async ({
  file_path,
  type = "image",
  sizes = { width: null, height: null },
  file,
  thumb = true,
}) => {
  let base64ContentArray = file.split(",");
  let mimeType = base64ContentArray[0].match(
    /[^:\s*]\w+\/[\w-+\d.]+(?=[;| ])/
  )[0];
  const extension = mimeType.split("/")[1];
  let filename = Math.random().toString(36).substr(2, 30);
  filename = `${ Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15) }.${type !== "image" ? "pdf" : extension}`;
  const finalFile = `${file_path}/${filename}`;
  const baseAssetsFolder = process.env.ASSETS_PATH;

  if (type === "image") {
    try {
      if (!exists(baseAssetsFolder, "dir")) {
        fs.mkdirSync(baseAssetsFolder, "0755");
      }

      if (!exists(file_path, "dir")) {
        fs.mkdirSync(file_path, { recursive: true });
      }

      const imageBuffer = Buffer.from(base64ContentArray[1], "base64");
      const imgBuff = await sharp(imageBuffer)
        .jpeg({
          quality: 100,
        })
        .toBuffer();

      await sharp(imgBuff).resize(sizes.width, sizes.height).toFile(finalFile);

      if (thumb) {
        await saveThumb(finalFile, filename, file_path);
      }

      return filename;
    } catch (error) {
      console.log(error);
    }
  } else {
    try {
      if (!exists(baseAssetsFolder, "dir")) {
        fs.mkdirSync(baseAssetsFolder, "0755");
      }

      if (!exists(file_path, "dir")) {
        fs.mkdirSync(file_path);
      }
      fs.writeFileSync(finalFile, base64ContentArray[1], {
        encoding: "base64",
      });

      return filename;
    } catch (error) {
      console.log(error);
    }
  }
};

/**
 * delete file
 */
export const deleteFile = async (file, file_path) => {
  if (file != "") {
    if (exists(`${file_path}/${file}`, "file")) {
      fs.unlinkSync(path.resolve(path.join(file_path, file)));
      console.log("deleted file -> ", path.resolve(path.join(file_path, file)));
    } else {
      console.log("File not exists.");
    }
  }
};

/**
 * compare lists to get updates
 */
export const getDelta = (source, updated) => {
  let added = updated.filter(
    (updatedItem) =>
      source.find((sourceItem) => sourceItem.id === updatedItem.id) ===
      undefined
  );
  let changed = updated.filter(
    (updatedItem) =>
      source.find((sourceItem) => sourceItem.id === updatedItem.id) !==
      undefined
  );
  let deleted = source.filter(
    (sourceItem) =>
      updated.find((updatedItem) => updatedItem.id === sourceItem.id) ===
      undefined
  );

  const delta = {
    added: added,
    changed: changed,
    deleted: deleted,
  };

  return delta;
};

export const onlyNumbers = (data) =>{
  if(data != ''){
    return data.replace(/\D/g,"");
  }
}
