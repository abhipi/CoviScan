# CoviScan- CNN based COVID-19 Variant Prediction
This native Android app uses a RESNET-18 model, trained using PyTorch, to detect COVID-19 with chest X-ray radiographs. The accuracy of the model was 98.9% on the COVID-QU-Ex Dataset and 73.8% across real world samples collected at the Powai Polyclinic and Hospital, Mumbai. The APK for this app can be downloaded using the link: https://drive.google.com/file/d/1VrsETSk48RTLPMqV0ly9HU3K8hj3MRuw/view?usp=sharing <br><br>
<img height="300" alt="image" src="https://github.com/abhipi/CoviScan/assets/75244191/817023a4-2f00-4b14-82cb-b27c9de13f02">
<img height="300" alt="image" src="https://github.com/abhipi/CoviScan/assets/75244191/c11c0862-44b8-4652-9517-439d6189bcf0">
<img height="300" width="500" alt="image" src="https://github.com/abhipi/CoviScan/assets/75244191/1a084b35-7871-4cdb-ab7d-471518528bd9">

## File Structure
* load_train_model.ipynb: Jupyter Notebook to load the dataset and train and test the 18-layer mobile CNN.
* CovidPredictor: Source code for the deployed Android application, with which the trained model is locally bundled.   
