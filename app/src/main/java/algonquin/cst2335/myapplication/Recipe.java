package algonquin.cst2335.myapplication;

    public class Recipe {
        private String id;
        private String title;
        private String image;
        private String summary;
        private String sourceUrl;

        public Recipe(String id, String title, String image, String summary, String sourceUrl) {
            this.id = id;
            this.title = title;
            this.image = image;
            this.summary = summary;
            this.sourceUrl = sourceUrl;
        }

        public Recipe(long id, String title, String image) {
        }


        // Getters and setters for each field (id, title, image, summary, sourceUrl)

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }
    }

